package com.example.managementapi.Mapper;

import com.example.managementapi.Dto.Request.ProductRequest.CreateProductRequest;
import com.example.managementapi.Dto.Request.ProductRequest.UpdateProductRequest;
import com.example.managementapi.Dto.Response.ProductResponse.CreateProductResponse;
import com.example.managementapi.Dto.Response.ProductResponse.GetProductsResponse;
import com.example.managementapi.Dto.Response.ProductResponse.ProductResponse;
import com.example.managementapi.Dto.Response.Supplier.GetSupplierDetailRes;
import com.example.managementapi.Entity.Product;
import com.example.managementapi.Entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(CreateProductRequest request);

    //get 1 product
    @Mapping(source = "suppliers.supplierName", target = "supplierName")
    ProductResponse toProductResponse(Product product);

    CreateProductResponse toCreateProductResponse(Product product);

    //get list product
    @Mapping(source = "suppliers.supplierName", target = "supplierName")
    GetProductsResponse toGetProductsResponses(Product products);

    void updateProduct(@MappingTarget Product product, UpdateProductRequest request);
    //xong bài #5

}
