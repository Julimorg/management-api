package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.CreateSupplierReq;
import com.example.managementapi.Dto.Request.UpdateSupplierReq;
import com.example.managementapi.Dto.Response.CreateSupplierRes;
import com.example.managementapi.Dto.Response.GetSupplierRes;
import com.example.managementapi.Dto.Response.UpdateSupplierRes;
import com.example.managementapi.Service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;


    @GetMapping("/get-suppliers")
    public ApiResponse<List<GetSupplierRes>> getSupplier(){
        return ApiResponse.<List<GetSupplierRes>>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(supplierService.getSuppliers())
                .build();
    }


    @PostMapping("/create-supplier")
    public ApiResponse<CreateSupplierRes> createSupplier(@RequestBody CreateSupplierReq request) {
        return ApiResponse.<CreateSupplierRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(supplierService.createSupplier(request))
                .build();
    }

    @PostMapping("/update-supplier/{supplier_id}")
    public ApiResponse<UpdateSupplierRes> updateSupplier(@RequestBody  String supplier_id, UpdateSupplierReq request) {
        return ApiResponse.<UpdateSupplierRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(supplierService.updateSupplier(supplier_id, request))
                .build();
    }

    @DeleteMapping("/delete-supplier/{supplierId}")
    public ApiResponse<String> deleteSupplier() {
        return ApiResponse.<String>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .message("Deleted Supplier Sucessfully!")
                .build();
    }



}
