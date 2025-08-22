package com.example.managementapi.Dto.Response.Supplier;


import com.example.managementapi.Entity.Color;
import com.example.managementapi.Entity.Product;
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
public class GetSupplierDetailRes {
    private String supplierId;
    private String supplierName;
    private String supplierAddress;
    private String supplierPhone;
    private String supplierEmail;
    private String supplierImg;

    private List<Product> products;

    private List<Color> colors;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
