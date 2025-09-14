package com.example.managementapi.Service;


import com.example.managementapi.Component.GenerateRandomCode;
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
        log.info("GET USER EMAIL:   " + user.getEmail());

        Order order = Order.builder()
                .user(user)
                .orderCode(orderCodeGenerator.generateOrderCode())
                .orderStatus(OrderStatus.Pending)
                .orderAmount(cart.getTotalPrice())
                .build();

        order = orderRepository.save(order);

        final Order finalOrder = order;

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(finalOrder)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getProductPrice())
                        .build())
                .toList();

        orderItemRepository.saveAll(orderItems);

        Payment payment = Payment
                .builder()
                .paymentMethod(PaymentMethod.CRASH)
                .amount(cart.getTotalPrice())
                .paymentStatus(String.valueOf(PaymentMethodStatus.Pending))
                .order(order)
                .build();

        paymentRepository.save(payment);

        finalOrder.setPayment(payment);
        finalOrder.setOrderItems(orderItems);


        String adminEmail = "kienphongtran2003@gmail.com";
        String storeName = "Cửa Hàng ABC";
        String orderManagementUrl = "https://yourstore.com/admin/orders/" + order.getOrderCode();
        String adminName = "Đội ngũ Admin";
        String processingDeadline = "24 giờ";



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
    public void approveOrder(String userId, String orderId, UpdateOrderReq request) throws MessagingException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> orderItemsList = order.getOrderItems();

        if(request.getOrderStatus() == OrderStatus.Successfully){
            emailService.sendOrderStatusEmail(
                    "lhquocbao1703@gmail.com", "Nguyễn Văn A", "ORDER123", "12/09/2025",
                    String.valueOf(request.getOrderStatus()), orderItemsList, null, "123 Đường ABC", "15/09/2025",
                    "Công Ty ABC", "support@abc.com", "0123456789", "www.abc.com"
            );
        }
    }
}
