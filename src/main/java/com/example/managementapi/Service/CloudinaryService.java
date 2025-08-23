package com.example.managementapi.Service;

import com.cloudinary.Cloudinary;
import com.example.managementapi.Dto.Response.Cloudinary.CloudinaryRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Transactional
    public CloudinaryRes uploadFile(MultipartFile file, String fileName) {
        try {
            log.info("Uploading file to Cloudinary with public_id: {}", "img" + fileName);
            final Map result = cloudinary
                    .uploader()
                    .upload(file.getBytes(), Map.of("public_id", "img" + fileName));
            final String url = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            log.info("File uploaded successfully. URL: {}, publicId: {}", url, publicId);
            return CloudinaryRes
                    .builder()
                    .publicId(publicId)
                    .url(url)
                    .build();
        } catch (IOException e) {
            log.error("Failed to upload file to Cloudinary: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during file upload: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error during file upload: " + e.getMessage(), e);
        }
    }
}