package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.Product.CreateProductReq;
import com.example.managementapi.Dto.Request.Product.UpdateProductReq;
import com.example.managementapi.Dto.Response.Cloudinary.CloudinaryRes;
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
import com.example.managementapi.Util.FileUpLoadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private final CloudinaryService cloudinaryService;

    //Tạo product
    public CreateProductRes createProduct(CreateProductReq request){
        if(productRepository.existsByProductName(request.getProductName())){
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        MultipartFile image = request.getProductImage();
        String imgUrl = null;

        if(image != null && !image.isEmpty()){
            FileUpLoadUtil.assertAllowed(image, FileUpLoadUtil.IMAGE_PATTERN);

            String fileName = FileUpLoadUtil.getFileName(request.getProductName());

            CloudinaryRes cloudinaryRes = cloudinaryService.uploadFile(image, fileName);

            imgUrl = cloudinaryRes.getUrl();
        }
        else {
            log.info("No image provided for product: {}", request.getProductName());
            throw new RuntimeException("Image is empty!");
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

        //*
        product.setProductImage(imgUrl);

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
        MultipartFile image = request.getProductImage();
        String imageUrl = null;

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        if(image != null && !image.isEmpty()) {
            FileUpLoadUtil.assertAllowed(image, FileUpLoadUtil.IMAGE_PATTERN);
            String fileName = FileUpLoadUtil.getFileName(request.getProductName());
            CloudinaryRes cloudinaryRes = cloudinaryService.uploadFile(image, fileName);
            imageUrl = cloudinaryRes.getUrl();
        }else {
            log.info("No image provided for product: {}", request.getProductName());
            throw new RuntimeException("Image is empty!");
        }

        productMapper.updateProduct(product, request);

        product.setProductImage(imageUrl);

        Product savedProduct = productRepository.save(product);

        UpdateProductRes response = productMapper.toUpdateProductRes(savedProduct);

        if (product.getSuppliers() != null) {
            response.setSupplierName(product.getSuppliers().getSupplierName());
        }

        if(product.getColors() != null){
            response.setColorName(product.getColors().getColorName());
        }

        return response;

//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//        product.setStatus(request.getStatus());
//        product.setQuantity(request.getQuantity());

    }

    public void deleteProduct(String id){
        productRepository.deleteById(id);
    }


}
