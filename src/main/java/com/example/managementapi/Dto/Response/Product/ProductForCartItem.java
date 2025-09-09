package com.example.managementapi.Dto.Response.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductForCartItem {
    private String productId;
    private String productName;
    private List<String> productImage;
    private String productVolume;
    private String productUnit;
    private String productCode;
    private int productQuantity;
    private double discount;
    private double productPrice;
    private String colorName;
    private String categoryName;
}
