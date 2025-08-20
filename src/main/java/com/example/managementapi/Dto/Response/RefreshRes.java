package com.example.managementapi.Dto.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshRes {
    private String token;
    private boolean authenticated;
}
