package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.Order.CreateOrderRequest;
import com.example.managementapi.Dto.Request.Order.GetProductQuantityRequest;
import com.example.managementapi.Dto.Response.Order.CreateOrderResponse;
import com.example.managementapi.Entity.*;
import com.example.managementapi.Enum.ErrorCode;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Mapper.OrderMapper;
import com.example.managementapi.Repository.OrderRepository;
import com.example.managementapi.Repository.ProductRepository;
import com.example.managementapi.Repository.UserRepository;
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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderMapper orderMapper;

    public CreateOrderResponse createOrder(String userId, CreateOrderRequest request){
        Order order = orderMapper.toOrder(request);
        order.setCreateAt(LocalDateTime.now());
        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;

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
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User does not exist"));
        order.setUser(user);

        Payment payment = new Payment();
        payment.setPaymentMethod(request.getPaymentMethod());

        order.setOrderItems(orderItems);
        order.setOrderAmount(totalAmount);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);
        CreateOrderResponse response = orderMapper.toCreateOrderResponse(savedOrder);
        response.setFirstName(savedOrder.getUser().getFirstName());

        return response;
    }

    public void deleteOrder(String id){
        orderRepository.deleteById(id);
    }
}
