package com.example.managementapi.Mapper.ProductMapper;

import com.example.managementapi.Dto.Request.ProductRequest.CreateProductRequest;
import com.example.managementapi.Dto.Request.ProductRequest.UpdateProductRequest;
import com.example.managementapi.Dto.Response.ProductResponse.ProductResponse;
import com.example.managementapi.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(CreateProductRequest request);
    ProductResponse toProductResponse(Product product);

    void updateProduct(@MappingTarget Product product, UpdateProductRequest request);
    //xong bài #5

}
