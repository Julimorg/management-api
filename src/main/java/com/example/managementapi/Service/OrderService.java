package com.example.managementapi.Service;


import com.example.managementapi.Component.GenerateRandomCode;
import com.example.managementapi.Dto.Request.Order.*;
import com.example.managementapi.Dto.Response.Order.*;
import com.example.managementapi.Dto.Response.Product.ProductForCartItem;
import com.example.managementapi.Entity.*;
import com.example.managementapi.Enum.OrderStatus;
import com.example.managementapi.Enum.PaymentMethod;
import com.example.managementapi.Enum.PaymentMethodStatus;
import com.example.managementapi.Mapper.OrderItemMapper;
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
import java.util.ArrayList;
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

    private final ProductRepository productRepository;

    private final EmailService emailService;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;


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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public CreateOrderResponse createOrder(String userId, CreateOrderRequest request) throws MessagingException {
        Order order = orderMapper.toOrder(request);
        order.setCreateAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.Pending);
        order.setOrderCode(orderCodeGenerator.generateOrderCode());
        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalQuantity = 0;

        for(GetProductQuantityRequest itemReq : request.getOrderItems()){
            Product product = productRepository.findById(itemReq.getProductId()).orElseThrow(() -> new RuntimeException("Product does not exist"));

            if(itemReq.getQuantity() > product.getProductQuantity()){
                throw new RuntimeException("Product quantity is not enough for this order");
            }

            productRepository.save(product);

            OrderItem orderItem = new OrderItem();


            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPrice(product.getProductPrice());
            orderItem.setCreateAt(LocalDateTime.now());

            orderItem.setOrder(order);

            orderItems.add(orderItem);

            BigDecimal totalItem = product.getProductPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(totalItem);
            totalQuantity += itemReq.getQuantity();
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User does not exist"));
        order.setUser(user);

        Payment payment = new Payment();
        payment.setPaymentMethod(PaymentMethod.CASH);

        order.setTotal_quantity(totalQuantity);
        order.setOrderItems(orderItems);
        order.setOrderAmount(totalAmount);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);
        CreateOrderResponse response = orderMapper.toCreateOrderResponse(savedOrder);
        response.setFirstName(savedOrder.getUser().getFirstName());

        GetOrderResponse orderResponse = orderMapper.toGetOrderResponse(savedOrder);

        emailService.sendOrderCreatedByAdminEmail(
                "lhquocbao1703@gmail.com",
                user.getFirstName(),
                savedOrder.getOrderCode(),
                savedOrder.getCreateAt(),
                savedOrder.getOrderStatus().name(),
                savedOrder.getOrderAmount(),
                savedOrder.getOrderItems(),
                "Tên công ty ABC",
                "support@abc.com",
                "0123-456-789",
                "https://abc.com"
        );

        return response;
    }

    public UpdateOrderByAdminResponse updateOrderByAdmin(String orderId, UpdateOrderByAdminRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderMapper.updateOrder(order, request);

//        if (request.getOrderItems() != null) {
//
//            //Xóa order item hiện tại
//            if (!order.getOrderItems().isEmpty()) {
//                orderItemRepository.deleteAll(order.getOrderItems());
//                order.getOrderItems().clear();
//            }
//
//            for (UpdateOrderItemByAdminRequest dto : request.getOrderItems()) {
//                OrderItem item = orderItemMapper.toOrderItem(dto);
//                item.setOrder(order);
//
//                Product product = productRepository.findById(dto.getProductId())
//                        .orElseThrow(() -> new RuntimeException("Product not found"));
//                item.setProduct(product);
//                item.setPrice(product.getProductPrice());
//
//                order.getOrderItems().add(item);
//            }
//
//            BigDecimal total = order.getOrderItems().stream()
//                    .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            order.setOrderAmount(total);
//        }

        orderRepository.save(order);
        return orderMapper.toUpdateOrderByAdminResponse(order);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF'")
    public void deleteOrder(String id){
        orderRepository.deleteById(id);
    }
}
