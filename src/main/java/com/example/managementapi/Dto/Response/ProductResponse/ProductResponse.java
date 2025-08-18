package com.example.managementapi.Dto.Response.ProductResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    String id;
    String name;
    String description;
    float price;
    String status;
    int quantity;
}
