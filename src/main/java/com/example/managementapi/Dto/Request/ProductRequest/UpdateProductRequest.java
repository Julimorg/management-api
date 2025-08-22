package com.example.managementapi.Dto.Request.ProductRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {
    private String productDescription;
    private String productImage;
    private String productVolume;
    private String productUnit;
    private String productCode;
    private int productQuantity;
    private double discount;
    private double productPrice;

}
