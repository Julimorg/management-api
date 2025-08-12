package com.example.managementapi.Service.ProductService;

import com.example.managementapi.Dto.Request.ProductRequest.CreateProductRequest;
import com.example.managementapi.Entity.Product;
import com.example.managementapi.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    //Tạo product
    public Product createProduct(CreateProductRequest request){
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStatus(request.getStatus());
        product.setQuantity(request.getQuantity());

        return productRepository.save(product);
    }

}
