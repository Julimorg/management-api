package com.example.managementapi.Dto.Response.Cart;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProductToCartRes {
    private String cartId;
    private String userId;
    private double totalPrice;
    private CartItemDetailRes items;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;


}
