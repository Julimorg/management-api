package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Supplier.CreateSupplierReq;
import com.example.managementapi.Dto.Request.Supplier.UpdateSupplierReq;
import com.example.managementapi.Dto.Response.Supplier.CreateSupplierRes;
import com.example.managementapi.Dto.Response.Supplier.GetSupplierDetailRes;
import com.example.managementapi.Dto.Response.Supplier.GetSupplierRes;
import com.example.managementapi.Dto.Response.Supplier.UpdateSupplierRes;
import com.example.managementapi.Service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
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

    @GetMapping("/detail-supplier/{supplierId}")
    public ApiResponse<GetSupplierDetailRes> getSupplierDetail(@PathVariable String supplierId){
        return ApiResponse.<GetSupplierDetailRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(supplierService.getSupplierDetailRes(supplierId) )
                .build();
    }

    @GetMapping("/search-supplier")
    public Page<GetSupplierRes> searchSupplier(
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 10, sort = "supplierName", direction = Sort.Direction.ASC) Pageable pageable
    ){
        return supplierService.searchSupplier(keyword, pageable);
    }


    @PostMapping(value = "/create-supplier", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CreateSupplierRes> createSupplier(
            //? Sử dụng @ModelAttribute để ánh xạ toàn bộ form-data vào CreateColorReq
            @ModelAttribute CreateSupplierReq request) {

        return ApiResponse.<CreateSupplierRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(supplierService.createSupplier(request))
                .build();
    }
    @PatchMapping(value = "/update-supplier/{supplierId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UpdateSupplierRes> updateSupplier(@PathVariable String supplierId, @ModelAttribute UpdateSupplierReq request) {

//        log.warn("supplier_id: " + supplierId);
        return ApiResponse.<UpdateSupplierRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(supplierService.updateSupplier(supplierId, request))
                .build();
    }

    @DeleteMapping("/delete-supplier/{supplierId}")
    public ApiResponse<String> deleteSupplier(@PathVariable String supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ApiResponse.<String>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message("Deleted Supplier Sucessfully!")
                .build();
    }



}
