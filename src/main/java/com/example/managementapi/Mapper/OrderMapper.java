package com.example.managementapi.Mapper;

import com.example.managementapi.Dto.Request.Order.CreateOrderRequest;
import com.example.managementapi.Dto.Response.Order.CreateOrderResponse;
import com.example.managementapi.Dto.Response.Order.GetOrderResponse;
import com.example.managementapi.Dto.Response.Order.GetOrdersResponse;
import com.example.managementapi.Entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(CreateOrderRequest request);

    CreateOrderResponse toCreateOrderResponse(Order order);

    //Get 1
    GetOrderResponse toGetOrderResponse(Order order);

    //Get list
    GetOrdersResponse toGetOrdersResponse(Order orders);
}
