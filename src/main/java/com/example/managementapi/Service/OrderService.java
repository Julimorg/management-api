package com.example.managementapi.Service;


import com.example.managementapi.Component.GenerateRandomCode;
import com.example.managementapi.Dto.Request.Order.ApproveOrderReq;
import com.example.managementapi.Dto.Request.Order.UpdateOrderReq;
import com.example.managementapi.Dto.Response.Order.*;
import com.example.managementapi.Dto.Response.Product.ProductForCartItem;
import com.example.managementapi.Entity.*;
import com.example.managementapi.Enum.OrderStatus;
import com.example.managementapi.Enum.PaymentMethod;
import com.example.managementapi.Enum.PaymentMethodStatus;
import com.example.managementapi.Mapper.OrderMapper;
import com.example.managementapi.Repository.*;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    private final GenerateRandomCode orderCodeGenerator;

    private final ProductRepository productRepository;

    private final EmailService emailService;

    private final OrderMapper orderMapper;

    private final String adminEmail = "kienphongtran2003@gmail.com";

    private final String storeName = "Cửa Hàng ABC";

    private final String orderManagementUrl = "https://yourstore.com/admin/orders/";

    private final String adminName = "Đội ngũ Admin";

    private final String processingDeadline = "24 giờ";

    private final PaymentRepository paymentRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<GetAllOrdersRes> getAllOrders(Pageable pageable){
        return orderRepository.findAll(pageable)
                .map(user -> orderMapper.toGetAllOrdersRes(user));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF','ROLE_USER')")
    public Page<GetUserOrdersRes> getUserOrders(String userId, Pageable pageable){

        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Order> ordersPage = orderRepository.findByUser(user, pageable);

        return ordersPage.map(orders -> orderMapper.toGetUserOrdersRes(orders));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF','ROLE_USER')")
    public GetUserOrdersDetailRes getUserOrderDetails(String userId, String orderId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order =  orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return orderMapper.toGetUserOrdersDetailRes(
                orderRepository.findByUserAndOrderId(user, orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found")));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF','ROLE_USER')")
    public GetOrderResponse createOrderFromCart(String userId, String cartId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot create order");
        }

        if (cart.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cart total price must be greater than 0");
        }

        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getQuantity() > cartItem.getProduct().getProductQuantity()) {
                throw new IllegalStateException("Product " + cartItem.getProduct().getProductName() + " is out of stock");
            }
        }

        Order order = Order.builder()
                .user(user)
                .orderCode(orderCodeGenerator.generateOrderCode())
                .shipAddress(user.getUserAddress())
                .orderStatus(OrderStatus.Pending)
                .total_quantity(cart.getTotalQuantity())
                .orderAmount(cart.getTotalPrice())
                .build();

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getProductPrice())
                        .build())
                .toList();

        Payment payment = Payment.builder()
                .paymentMethod(PaymentMethod.CASH)
                .amount(cart.getTotalPrice())
                .paymentStatus(PaymentMethodStatus.Pending.name())
                .order(order)
                .build();

        order.setOrderItems(orderItems);
        order.setPayment(payment);
        orderRepository.save(order);

        GetOrderResponse orderResponse = GetOrderResponse.builder()
                .orderId(order.getOrderId())
                .orderCode(order.getOrderCode())
                .userId(userId)
                .status(order.getOrderStatus())
                .amount(order.getOrderAmount())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userAddress(user.getUserAddress())
                .orderItems(order.getOrderItems().stream()
                        .map(or -> OrderItemRes.builder()
                                .orderItemId(or.getOrderItemId())
                                .orderId(or.getOrder().getOrderId())
                                .quantity(or.getQuantity())
                                .product(ProductForCartItem.builder()
                                        .productId(or.getProduct().getProductId())
                                        .productName(or.getProduct().getProductName())
                                        .productImage(or.getProduct().getProductImage())
                                        .productVolume(or.getProduct().getProductVolume())
                                        .productUnit(or.getProduct().getProductUnit())
                                        .productCode(or.getProduct().getProductCode())
                                        .productQuantity(or.getQuantity())
                                        .discount(or.getProduct().getDiscount())
                                        .productPrice(or.getProduct().getProductPrice())
                                        .colorName(or.getProduct().getColors().getColorCode())
                                        .categoryName(or.getProduct().getCategory().getCategoryName())
                                        .build())
                                .createAt(or.getCreateAt())
                                .updateAt(or.getUpdateAt())
                                .build())
                        .toList())
                .paymentId(payment.getPaymentId())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .createAt(order.getCreateAt())
                .updateAt(order.getUpdateAt())
                .build();


        return orderResponse;

    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_STAFF')")
    public GetOrderResponse updateOrderFromUser(String userId, String orderId, UpdateOrderReq request){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order userOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!userOrder.getUser().getId().equals(userId)) {
            throw new RuntimeException("Order " + orderId + " not found in user " + user.getUserName());
        }

        Payment payment = userOrder.getPayment();

        userOrder.getPayment().setPaymentMethod(request.getPaymentMethod());
        userOrder.setShipAddress(request.getShipAddress());

        payment.setPaymentStatus(String.valueOf(PaymentMethodStatus.Successfully));

        paymentRepository.save(payment);

        orderRepository.save(userOrder);


        GetOrderResponse orderResponse = GetOrderResponse.builder()
                .orderId(userOrder.getOrderId())
                .orderCode(userOrder.getOrderCode())
                .userId(userId)
                .status(userOrder.getOrderStatus())
                .amount(userOrder.getOrderAmount())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userAddress(user.getUserAddress())
                .shipAddress(userOrder.getShipAddress())
                .orderItems(userOrder.getOrderItems().stream()
                        .map(or -> OrderItemRes.builder()
                                .orderItemId(or.getOrderItemId())
                                .orderId(or.getOrder().getOrderId())
                                .quantity(or.getQuantity())
                                .product(ProductForCartItem.builder()
                                        .productId(or.getProduct().getProductId())
                                        .productName(or.getProduct().getProductName())
                                        .productImage(or.getProduct().getProductImage())
                                        .productVolume(or.getProduct().getProductVolume())
                                        .productUnit(or.getProduct().getProductUnit())
                                        .productCode(or.getProduct().getProductCode())
                                        .productQuantity(or.getQuantity())
                                        .discount(or.getProduct().getDiscount())
                                        .productPrice(or.getProduct().getProductPrice())
                                        .colorName(or.getProduct().getColors().getColorCode())
                                        .categoryName(or.getProduct().getCategory().getCategoryName())
                                        .build())
                                .createAt(or.getCreateAt())
                                .updateAt(or.getUpdateAt())
                                .build())
                        .toList())
                .paymentId(payment.getPaymentId())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .createAt(userOrder.getCreateAt())
                .updateAt(userOrder.getUpdateAt())
                .build();

        emailService.sendOrderNotificationToAdmin(adminEmail,
                orderResponse,
                storeName,
                orderManagementUrl,
                adminName,
                processingDeadline);


        return orderResponse;

    }


    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public void approveOrder(String userId, String orderId, ApproveOrderReq request) throws MessagingException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));


        List<OrderItem> orderItemsList = order.getOrderItems();

        Cart cart = user.getCart();


        GetOrderResponse orderResponse = GetOrderResponse.builder()
                .orderId(order.getOrderId())
                .orderCode(order.getOrderCode())
                .status(order.getOrderStatus())
                .amount(order.getOrderAmount())
                .userId(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userAddress(user.getUserAddress())
                .shipAddress(order.getShipAddress())
                .orderItems(orderItemsList.stream()
                        .map(item -> OrderItemRes.builder()
                                .orderItemId(item.getOrderItemId())
                                .orderId(item.getOrder().getOrderId())
                                .quantity(item.getQuantity())
                                .product(ProductForCartItem.builder()
                                        .productId(item.getProduct().getProductId())
                                        .productName(item.getProduct().getProductName())
                                        .productImage(item.getProduct().getProductImage())
                                        .productVolume(item.getProduct().getProductVolume())
                                        .productUnit(item.getProduct().getProductUnit())
                                        .productCode(item.getProduct().getProductCode())
                                        .productQuantity(item.getProduct().getProductQuantity())
                                        .discount(item.getProduct().getDiscount())
                                        .productPrice(item.getProduct().getProductPrice())
                                        .colorName(item.getProduct().getColors().getColorName())
                                        .categoryName(item.getProduct().getCategory().getCategoryName())
                                        .build())
                                .createAt(item.getCreateAt())
                                .updateAt(item.getUpdateAt())
                                .build())
                        .collect(Collectors.toList()))
                .paymentId(order.getPayment().getPaymentId())
                .paymentMethod(order.getPayment().getPaymentMethod())
                .paymentStatus(order.getPayment().getPaymentStatus())
                .createAt(order.getCreateAt())
                .updateAt(order.getUpdateAt())
                .completeAt(order.getCompleteAt())
                .build();


        if(request.getOrderStatus() == OrderStatus.Approved){

            order.setOrderStatus(OrderStatus.Approved);

            order.setUpdateAt(LocalDateTime.now());

            order.setCompleteAt(LocalDateTime.now());

            orderRepository.save(order);

            for (OrderItem orderItem : orderItemsList) {
                Product product = orderItem.getProduct();
                if (product == null) {
                    throw new RuntimeException("Product not found for order item");
                }
                int newQuantity = product.getProductQuantity() - orderItem.getQuantity();
                if (newQuantity < 0) {
                    throw new RuntimeException("Insufficient product quantity for " + product.getProductName());
                }
                product.setProductQuantity(newQuantity);
                productRepository.save(product);
            }

            cart.getCartItems().clear();
            cart.setTotalQuantity(0);
            cart.setTotalPrice(BigDecimal.ZERO);

            cartRepository.save(cart);

            emailService.sendOrderApprovedEmail(orderResponse);
        } else if (request.getOrderStatus() == OrderStatus.Canceled) {

            order.setOrderStatus(OrderStatus.Canceled);
            order.setUpdateAt(LocalDateTime.now());
            order.setDeletedAt(LocalDateTime.now());

            cart.getCartItems().clear();
            cart.setTotalQuantity(0);
            cart.setTotalPrice(BigDecimal.ZERO);

            order.setCompleteAt(LocalDateTime.now());

            emailService.sendOrderCanceledEmail(orderResponse);
        } else {
            throw new RuntimeException("Unsupported order status: " + request.getOrderStatus());
        }
    }
}
