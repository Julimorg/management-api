package com.example.managementapi.Dto.Request.Supplier;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSupplierReq {
    private String supplierName;
    private String supplierAddress;
    private String supplierPhone;
    private String supplierEmail;
    private String supplierImg;

}
