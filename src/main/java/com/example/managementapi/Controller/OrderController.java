package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Cart.AddItemToCartReq;
import com.example.managementapi.Dto.Request.Cart.UpdateCartItemQuantityReq;
import com.example.managementapi.Dto.Request.Order.CreateOrderRequest;
import com.example.managementapi.Dto.Request.Order.UpdateOrderReq;
import com.example.managementapi.Dto.Response.Cart.CartItemDetailRes;
import com.example.managementapi.Dto.Response.Cart.GetCartRes;
import com.example.managementapi.Dto.Response.Order.CreateOrderResponse;
import com.example.managementapi.Dto.Response.Order.GetOrderResponse;
import com.example.managementapi.Service.CartService;
import com.example.managementapi.Service.OrderService;
import com.example.managementapi.Service.OrderVersionService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    private OrderVersionService orderVersionService;


    @PostMapping("/from-cart/{userId}/{cartId}")
    public ApiResponse<GetOrderResponse> addProductToCart(@PathVariable String userId,
                                                          @PathVariable String cartId){
        return ApiResponse.<GetOrderResponse>builder()
                .status_code(HttpStatus.OK.value())
                .message("Create Order Successfully!")
                .data(orderService.createOrderFromCart(userId, cartId))
                .timestamp(new Date())
                .build();
    }
    @PatchMapping("/update-order/{userId}/{orderId}")
    public ApiResponse<String> updateCartItem(@PathVariable String userId,
                                              @PathVariable String orderId,
                                              @Valid @RequestBody UpdateOrderReq request) throws MessagingException {
        orderService.approveOrder(userId, orderId, request);
        return ApiResponse.<String>builder()
                .status_code(HttpStatus.OK.value())
                .message("Update Order Successfully!")
                .build();
    }
//    @GetMapping("/get-cart/{userId}")
//    public ApiResponse<GetCartRes> getCart(@PathVariable String userId){
//        return ApiResponse.<GetCartRes>builder()
//                .status_code(HttpStatus.OK.value())
//                .message(HttpStatus.OK.getReasonPhrase())
//                .data(cartService.getCart(userId))
//                .build();
//    }
//

//
//    @DeleteMapping("/delete-item/{cartItemId}")
//    public ApiResponse<String> deleteItem(@PathVariable String cartItemId){
//        cartService.deleteCartItem(cartItemId);
//        return ApiResponse.<String>
//                builder()
//                .status_code(HttpStatus.OK.value())
//                .message("Delete item: " + cartItemId + " successfully! ")
//                .build();
//    }

    @PostMapping("/create-order/{userId}")
    public ApiResponse<CreateOrderResponse> createOrder(@PathVariable String userId, @RequestBody CreateOrderRequest request){
        return ApiResponse.<CreateOrderResponse>builder()
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(orderService.createOrder(userId,request))
                .build();
    }

    @DeleteMapping("delete/{orderId}")
    public ApiResponse<String> deleteOrder(@PathVariable String orderId){
        orderService.deleteOrder(orderId);
        return ApiResponse.<String>builder()
                .status_code(HttpStatus.OK.value())
                .message("Deleted order ID: " + orderId)
                .build();
    }
}
