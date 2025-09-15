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
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    private final GenerateRandomCode orderCodeGenerator;

    private final OrderItemRepository orderItemRepository;

    private final PaymentRepository paymentRepository;

    private final EmailService emailService;

    private final OrderMapper orderMapper;


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

        orderRepository.save(userOrder);

        String adminEmail = "kienphongtran2003@gmail.com";
        String storeName = "Cửa Hàng ABC";
        String orderManagementUrl = "https://yourstore.com/admin/orders/" + userOrder.getOrderCode();
        String adminName = "Đội ngũ Admin";
        String processingDeadline = "24 giờ";


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

        if(request.getOrderStatus() == OrderStatus.Approved){
            emailService.sendOrderStatusEmail(
                    "lhquocbao1703@gmail.com", "Nguyễn Văn A", "ORDER123", "12/09/2025",
                    String.valueOf(request.getOrderStatus()), orderItemsList, null, "123 Đường ABC", "15/09/2025",
                    "Công Ty ABC", "support@abc.com", "0123456789", "www.abc.com"
            );
        }
    }
}
