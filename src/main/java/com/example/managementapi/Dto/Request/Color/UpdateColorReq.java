package com.example.managementapi.Dto.Request.Color;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateColorReq {
    private String colorName;
    private String colorCode;
    private String colorImg;
    private String colorDescription;
}
