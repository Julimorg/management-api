package com.example.managementapi.Controller.ProductController;

import com.example.managementapi.Dto.Request.ProductRequest.CreateProductRequest;
import com.example.managementapi.Dto.Request.ProductRequest.UpdateProductRequest;
import com.example.managementapi.Entity.Product;
import com.example.managementapi.Service.ProductService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    Product createProduct(@RequestBody CreateProductRequest request){
        return productService.createProduct(request);
    }

    @GetMapping
    List<Product> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{productId}")
    Product getProduct(@PathVariable("productId") String productId){
        return productService.getProduct(productId);
    }

    @PutMapping("/{productId}")
    Product updateProduct(@PathVariable("productId") String productId, @RequestBody UpdateProductRequest request){
        return productService.updateProduct(productId, request);
    }

    @DeleteMapping("/{productId}")
    String deleteProduct(@PathVariable("productId") String productId){
        productService.deleteProduct(productId);

        return "Product has been deleted";
    }

}
