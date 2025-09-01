package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.Product.CreateProductReq;
import com.example.managementapi.Dto.Request.Product.UpdateProductReq;
import com.example.managementapi.Dto.Response.Product.CreateProductRes;
import com.example.managementapi.Dto.Response.Product.GetProductsRes;
import com.example.managementapi.Dto.Response.Product.ProductRes;
import com.example.managementapi.Dto.Response.Product.UpdateProductRes;
import com.example.managementapi.Entity.Color;
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

    public CreateProductRes createProduct(CreateProductReq request){
        if(productRepository.existsByProductName(request.getProductName())){
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        Product product = productMapper.toProduct(request);

        if(request.getSupplierId() != null){
            Supplier supplier = supplierRepository.findById(request.getSupplierId()).orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED));
            product.setSuppliers(supplier);
        }

        if(request.getColorId() != null){
            Color color = colorRepository.findById(request.getColorId()).orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_EXISTED));
            product.setColors(color);
        }

        Product savedProduct = productRepository.save(product);

        CreateProductRes response = productMapper.toCreateProductResponse(savedProduct);

        if(savedProduct.getSuppliers() != null){
            response.setSupplierName(savedProduct.getSuppliers().getSupplierName());
        }

        if(savedProduct.getColors() != null){
            response.setColorName(savedProduct.getColors().getColorName());
        }

        return response;
//        product.setName(request.getName());
//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//        product.setStatus(request.getStatus());
//        product.setQuantity(request.getQuantity());
    }

    //Get list product
    //Note: get thủ công cho supplier
    // Check xem findALl xem co lay them nhung thang ENtity ko lien quan ko
    public List<GetProductsRes> getProducts(){
        return productRepository.findAll().stream().map(product -> productMapper.toGetProductsResponses(product)).toList();
    }

    //Get 1 product
    public ProductRes getProduct(String id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductRes response = productMapper.toProductResponse(product);

        if (product.getSuppliers() != null) {
            response.setSupplierName(product.getSuppliers().getSupplierName());
        }

        if(product.getColors() != null){
            response.setColorName(product.getColors().getColorName());
        }

        return response;
    }

    //Update Product
    public UpdateProductRes updateProduct(String id, UpdateProductReq request){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        productMapper.updateProduct(product, request);

        Product savedProduct = productRepository.save(product);

        UpdateProductRes res = productMapper.toUpdateProductRes(savedProduct);

        if(savedProduct.getSuppliers() != null){
            //
        }

        return productMapper.toUpdateProductRes(productRepository.save(product));

//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//        product.setStatus(request.getStatus());
//        product.setQuantity(request.getQuantity());

    }

    public void deleteProduct(String id){
        productRepository.deleteById(id);
    }


}
