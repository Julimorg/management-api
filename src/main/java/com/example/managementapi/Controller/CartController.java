package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Cart.AddItemToCartReq;
import com.example.managementapi.Dto.Response.Cart.GetCartRes;
import com.example.managementapi.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-items/{id}")
    public ApiResponse<GetCartRes> addProductToCart(@PathVariable String id, @RequestBody AddItemToCartReq req){
        return ApiResponse.<GetCartRes>builder()
                .status_code(HttpStatus.OK.value())
                .message("Adding Product Successfully")
                .data(cartService.addProductToCart(id, req))
                .build();
    }

    @GetMapping("/get-cart/{cartId}")
    public ApiResponse<GetCartRes> getCart(@PathVariable String cartId){
        return ApiResponse.<GetCartRes>builder()
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(cartService.getCart(cartId))
                .build();
    }
}
