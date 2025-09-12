package com.example.managementapi.Service;


import com.example.managementapi.Component.GenerateRandomCode;
import com.example.managementapi.Dto.Request.Order.UpdateOrderReq;
import com.example.managementapi.Dto.Response.Cart.CartItemDetailRes;
import com.example.managementapi.Dto.Response.Order.GetOrderResponse;
import com.example.managementapi.Dto.Response.Order.OrderItemRes;
import com.example.managementapi.Dto.Response.Product.ProductForCartItem;
import com.example.managementapi.Entity.*;
import com.example.managementapi.Enum.OrderStatus;
import com.example.managementapi.Enum.PaymentMethod;
import com.example.managementapi.Enum.PaymentMethodStatus;
import com.example.managementapi.Repository.*;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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

    private final EmailService emailService;

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF','ROLE_USER')")
    public GetOrderResponse createOrderFromCart(String userId, String cartId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));


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

        List<OrderItemRes> orderItemResList = order.getOrderItems().stream()
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
                .toList();


        return GetOrderResponse
                .builder()
                .orderId(order.getOrderId())
                .orderCode(order.getOrderCode())
                .userId(userId)
                .status(order.getOrderStatus())
                .amount(order.getOrderAmount())
                .orderItems(orderItemResList)
                .paymentId(payment.getPaymentId())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .build();

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
