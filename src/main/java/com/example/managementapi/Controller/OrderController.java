package com.example.managementapi.Controller;

import com.cloudinary.Api;
import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Order.UpdateOrderReq;
import com.example.managementapi.Dto.Response.Order.GetOrderResponse;
import com.example.managementapi.Dto.Response.Order.GetAllOrdersRes;
import com.example.managementapi.Dto.Response.Order.GetUserOrdersDetailRes;
import com.example.managementapi.Dto.Response.Order.GetUserOrdersRes;
import com.example.managementapi.Service.OrderService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;


    @GetMapping("/user-order")
    public ApiResponse<Page<GetAllOrdersRes>> getAllOrders(
            @PageableDefault(size = 10, sort = "userId"
                    , direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.<Page<GetAllOrdersRes>>builder()
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(orderService.getAllOrders(pageable))
                .timestamp(new Date())
                .build();
    }

    @GetMapping("/user-order/{userId}")
    public ApiResponse<Page<GetUserOrdersRes>> getUserOrders(
            @PathVariable String userId,
            @PageableDefault(size = 10, sort = "userId"
                    , direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.<Page<GetUserOrdersRes>>builder()
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(orderService.getUserOrders(userId, pageable))
                .timestamp(new Date())
                .build();
    }

    @GetMapping("/user-order-detail/{userId}/{orderId}")
    public ApiResponse<GetUserOrdersDetailRes> getUserOrders(
            @PathVariable String userId,
            @PathVariable String orderId) {
        return ApiResponse.<GetUserOrdersDetailRes>builder()
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(orderService.getUserOrderDetails(userId,orderId))
                .timestamp(new Date())
                .build();
    }

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
}
