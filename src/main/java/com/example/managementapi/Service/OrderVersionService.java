package com.example.managementapi.Service;

import com.example.managementapi.Component.GenerateRandomCode;
import com.example.managementapi.Dto.Request.Order.CreateOrderRequest;
import com.example.managementapi.Dto.Request.Order.GetProductQuantityRequest;
import com.example.managementapi.Dto.Response.Order.CreateOrderResponse;
import com.example.managementapi.Dto.Response.Order.GetOrderResponse;
import com.example.managementapi.Entity.*;
import com.example.managementapi.Enum.ErrorCode;
import com.example.managementapi.Enum.OrderStatus;
import com.example.managementapi.Enum.PaymentMethod;
import com.example.managementapi.Enum.PaymentMethodStatus;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Mapper.OrderMapper;
import com.example.managementapi.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderVersionService {
    @Autowired
    private OrderRepository orderRepository;

    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GenerateRandomCode generateRandomCode;

    public GetOrderResponse copyCartToOrder(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User does not found"));
        Cart cart = user.getCart();

        Order newOrder = Order.builder()
                .orderCode(generateRandomCode.generateOrderCode())
                .orderStatus(OrderStatus.Pending)
                .createAt(LocalDateTime.now())
                .orderAmount(cart.getTotalPrice())
                .user(user)
                .build();

        newOrder = orderRepository.save(newOrder);
        final Order order = newOrder;

        List<OrderItem> orderItems = cart.getCartItems()
                .stream().map(item -> OrderItem.builder()
                        .price(item.getProduct().getProductPrice())
                        .quantity(item.getQuantity())
                        .product(item.getProduct())
                        .order(order)
                        .build()).toList();

        Payment payment = Payment.builder()
                .paymentMethod(PaymentMethod.CRASH)
                .paymentStatus(String.valueOf(PaymentMethodStatus.Pending))
                .amount(cart.getTotalPrice())
                .order(order)
                .build();

        order.setOrderItems(orderItems);
        order.setPayment(payment);

        return orderMapper.toGetOrderResponse(order);

    }


}
