package com.example.managementapi.Dto.Response.Cart;


import com.example.managementapi.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCartRes {
    private String cartId;
    private String userId;
    private double totalPrice;
    private List<CartItemDetailRes> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
