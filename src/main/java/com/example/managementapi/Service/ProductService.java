package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.ProductRequest.CreateProductRequest;
import com.example.managementapi.Dto.Request.ProductRequest.UpdateProductRequest;
import com.example.managementapi.Dto.Response.ProductResponse.CreateProductResponse;
import com.example.managementapi.Dto.Response.ProductResponse.GetProductsResponse;
import com.example.managementapi.Dto.Response.ProductResponse.ProductResponse;
import com.example.managementapi.Entity.Category;
import com.example.managementapi.Entity.Product;
import com.example.managementapi.Entity.Supplier;
import com.example.managementapi.Enum.ErrorCode;
import com.example.managementapi.Exception.AppException;
import com.example.managementapi.Mapper.ProductMapper;
import com.example.managementapi.Repository.ColorRepository;
import com.example.managementapi.Repository.ProductRepository;
import com.example.managementapi.Repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ColorRepository colorRepository;


    @Autowired
    private ProductMapper productMapper;

    //Tạo product
    public CreateProductResponse createProduct(CreateProductRequest request){
        if(productRepository.existsByProductName(request.getProductName())){
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        Product product = productMapper.toProduct(request);
        if(request.getSupplierId() != null){
            Supplier supplier = supplierRepository.findById(request.getSupplierId()).orElseThrow(()-> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED));
            product.setSuppliers(supplier);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toCreateProductResponse(savedProduct);
//        product.setName(request.getName());
//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//        product.setStatus(request.getStatus());
//        product.setQuantity(request.getQuantity());
    }

    //Get list product
    public List<GetProductsResponse> getProducts(){
        return productRepository.findAll().stream().map(product -> productMapper.toGetProductsResponses(product)).toList();
    }

    //Get 1 product
    public ProductResponse getProduct(String id){
        return productMapper.toProductResponse(productRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Product not found")));
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
