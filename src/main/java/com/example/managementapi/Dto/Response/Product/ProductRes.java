package com.example.managementapi.Dto.Response.Product;

import com.example.managementapi.Entity.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRes {
    private String productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private String productVolume;
    private String productUnit;
    private String productCode;
    private int productQuantity;
    private double discount;
    private double productPrice;

    //
    private String supplierName;
    private String colorName;
}
