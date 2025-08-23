package com.example.managementapi.Dto.Response.Cloudinary;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloudinaryRes {
    private String publicId;

    private String url;
}
