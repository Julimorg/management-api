package com.example.managementapi.Controller;

import com.example.managementapi.Dto.Request.ProductRequest.CreateProductRequest;
import com.example.managementapi.Dto.Request.ProductRequest.UpdateProductRequest;
import com.example.managementapi.Dto.Response.ProductResponse.CreateProductResponse;
import com.example.managementapi.Dto.Response.ProductResponse.GetProductsResponse;
import com.example.managementapi.Dto.Response.ProductResponse.ProductResponse;
import com.example.managementapi.Entity.Product;
import com.example.managementapi.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/create-product")
    CreateProductResponse createProduct(@RequestBody CreateProductRequest request){
        return productService.createProduct(request);
    }

    @GetMapping("/get-products")
    List<GetProductsResponse> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/detail-product/{productId}")
    ProductResponse getProduct(@PathVariable("productId") String productId){
        return productService.getProduct(productId);
    }

    @PutMapping("/update/{productId}")
    ProductResponse updateProduct(@PathVariable("productId") String productId, @RequestBody UpdateProductRequest request){
        return productService.updateProduct(productId, request);
    }

    @DeleteMapping("/delete/{productId}")
    String deleteProduct(@PathVariable("productId") String productId){
        productService.deleteProduct(productId);

        return "Product has been deleted";
    }

}
