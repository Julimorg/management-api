package com.example.managementapi.Controller;

import com.cloudinary.Api;
import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Product.CreateProductReq;
import com.example.managementapi.Dto.Request.Product.UpdateProductReq;
import com.example.managementapi.Dto.Response.Product.*;
import com.example.managementapi.Service.ProductService;
import com.example.managementapi.Util.QRGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/create-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<CreateProductRes> createProduct(@ModelAttribute CreateProductReq request){
        return ApiResponse.<CreateProductRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(productService.createProduct(request))
                .build();
    }

    @GetMapping("/get-products")
    ApiResponse<List<GetProductsRes>> getProducts(){
        return ApiResponse.<List<GetProductsRes>>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(productService.getProducts())
                .build();
    }

    @GetMapping("/detail-product/{productId}")
    ApiResponse<ProductRes> getProduct(@PathVariable("productId") String productId){
        return ApiResponse.<ProductRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(productService.getProduct(productId))
                .build();

    }

    @PatchMapping(value = "/update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<UpdateProductRes> updateProduct(@PathVariable("productId") String productId, @ModelAttribute UpdateProductReq request){
        return ApiResponse.<UpdateProductRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(productService.updateProduct(productId, request))
                .build();
    }

    @DeleteMapping("/delete/{productId}")
    ApiResponse<String> deleteProduct(@PathVariable("productId") String productId){
        productService.deleteProduct(productId);

        return ApiResponse.<String>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .build();
    }

    @GetMapping("/generate-qr/{productId}")
    public ApiResponse<String> generateProductQr(@PathVariable String productId) {
        ProductRes product = productService.getProduct(productId);

        String productJson = new QRGenerateUtil().prettyObject(product);

        String qrCodeBase64 = QRGenerateUtil.generateQrCode(productJson, 300, 300);

        return ApiResponse.<String>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(qrCodeBase64)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<SearchProductRes>> searchProducts(
            @RequestParam(value = "keyword", defaultValue = "") String keyword, Pageable pageable){

        Page<SearchProductRes> product = productService.searchProducts(keyword, pageable);

        return ApiResponse.<Page<SearchProductRes>>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(product)
                .build();
    }
}
