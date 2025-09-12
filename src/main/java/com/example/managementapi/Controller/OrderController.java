package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Cart.AddItemToCartReq;
import com.example.managementapi.Dto.Request.Cart.UpdateCartItemQuantityReq;
import com.example.managementapi.Dto.Response.Cart.CartItemDetailRes;
import com.example.managementapi.Dto.Response.Cart.GetCartRes;
import com.example.managementapi.Dto.Response.Order.GetOrderResponse;
import com.example.managementapi.Service.CartService;
import com.example.managementapi.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/from-cart/{userId}/{cartId}")
    public ApiResponse<GetOrderResponse> addProductToCart(@PathVariable String userId,
                                                          @PathVariable String cartId){
        return ApiResponse.<GetOrderResponse>builder()
                .status_code(HttpStatus.OK.value())
                .message("Adding Product Successfully")
                .data(orderService.createOrderFromCart(userId, cartId))
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
//    @PatchMapping("/update-item/{cartItemId}")
//    public ApiResponse<CartItemDetailRes> updateCartItem(@PathVariable String cartItemId
//            , @RequestBody UpdateCartItemQuantityReq request){
//        return ApiResponse.<CartItemDetailRes>builder()
//                .status_code(HttpStatus.OK.value())
//                .message(HttpStatus.OK.getReasonPhrase())
//                .data(cartService.updateCartItem(cartItemId, request))
//                .build();
//    }
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
}
