package com.example.managementapi.Service.ProductService;

import com.example.managementapi.Dto.Request.ProductRequest.CreateProductRequest;
import com.example.managementapi.Dto.Request.ProductRequest.UpdateProductRequest;
import com.example.managementapi.Dto.Response.ProductResponse.ProductResponse;
import com.example.managementapi.Entity.Product;
import com.example.managementapi.Enum.ErrorCode.ErrorCode;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Mapper.ProductMapper.ProductMapper;
import com.example.managementapi.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    //Tạo product
    public Product createProduct(CreateProductRequest request){


        if(productRepository.existsByProductName(request.getName())){
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        Product product = productMapper.toProduct(request);

//        product.setName(request.getName());
//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//        product.setStatus(request.getStatus());
//        product.setQuantity(request.getQuantity());

        return productRepository.save(product);
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public ProductResponse getProduct(String id){
        return productMapper.toProductResponse(productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found")));
    }

    public ProductResponse updateProduct(String id, UpdateProductRequest request){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productMapper.updateProduct(product, request);

//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//        product.setStatus(request.getStatus());
//        product.setQuantity(request.getQuantity());

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public void deleteProduct(String id){
        productRepository.deleteById(id);
    }
}
