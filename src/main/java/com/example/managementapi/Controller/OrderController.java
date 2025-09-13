package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Order.CreateOrderRequest;
import com.example.managementapi.Dto.Response.Order.CreateOrderResponse;
import com.example.managementapi.Service.OrderService;
import com.example.managementapi.Service.OrderVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    @Autowired
    private OrderVersionService orderVersionService;

    @PostMapping("/create-order/{userId}")
    public ApiResponse<CreateOrderResponse> createOrder(@PathVariable String userId, @RequestBody CreateOrderRequest request){
        return ApiResponse.<CreateOrderResponse>builder()
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(orderVersionService.createOrder(userId,request))
                .build();
    }

    @DeleteMapping("delete/{orderId}")
    public ApiResponse<String> deleteOrder(@PathVariable String orderId){
        orderVersionService.deleteOrder(orderId);
        return ApiResponse.<String>builder()
                .status_code(HttpStatus.OK.value())
                .message("Deleted order ID: " + orderId)
                .build();
    }
}
