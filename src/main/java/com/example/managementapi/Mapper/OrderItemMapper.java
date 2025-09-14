package com.example.managementapi.Mapper;


import com.example.managementapi.Dto.Response.Order.OrderItemRes;
import com.example.managementapi.Entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {
    OrderItemRes toOrderItemRes(OrderItem orderItem);
    List<OrderItemRes> toOrderItemResList(List<OrderItem> orderItems);
}
