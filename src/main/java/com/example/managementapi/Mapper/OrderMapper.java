package com.example.managementapi.Mapper;


import com.example.managementapi.Dto.Request.Order.CreateOrderRequest;
import com.example.managementapi.Dto.Response.Order.*;
import com.example.managementapi.Dto.Response.Product.ProductForCartItem;
import com.example.managementapi.Entity.Order;
import com.example.managementapi.Entity.OrderItem;
import com.example.managementapi.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    GetOrderResponse toGetOrderResponse(Order order);
    OrderItemRes toOrderItemRes(OrderItem orderItem);
    ProductForCartItem toProductForCartItem(Product product);


    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderCode", target = "orderCode")
    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "orderAmount", target = "amount")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.userAddress", target = "userAddress")
    @Mapping(source = "payment.paymentMethod", target = "paymentMethod")
    GetAllOrdersRes toGetAllOrdersRes(Order order);


    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderCode", target = "orderCode")
    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "orderAmount", target = "amount")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.userAddress", target = "userAddress")
    @Mapping(source = "payment.paymentMethod", target = "paymentMethod")
    GetUserOrdersRes toGetUserOrdersRes(Order order);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderCode", target = "orderCode")
    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "orderAmount", target = "amount")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.userAddress", target = "userAddress")
    @Mapping(source = "payment.paymentMethod", target = "paymentMethod")
    @Mapping(source = "orderItems", target = "items")
    GetUserOrdersDetailRes toGetUserOrdersDetailRes(Order order);


    Order toOrder(CreateOrderRequest request);
    CreateOrderResponse toCreateOrderResponse(Order order);
    //Get list
    GetOrdersResponse toGetOrdersResponse(Order orders);


}
