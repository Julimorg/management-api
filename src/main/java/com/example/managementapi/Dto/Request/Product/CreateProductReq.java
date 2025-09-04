package com.example.managementapi.Dto.Request.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductReq {
    private String productName;
    private String productDescription;
    private MultipartFile productImage;
    private String productVolume;
    private String productUnit;
    private String productCode;
    private int productQuantity;
    private double discount;
    private double productPrice;

    private String supplierId;
    private String colorId;
    private String categoryId;

}
