package com.example.managementapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.util.Date;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private int code = 1000;
    private int status_code;
    private String message;
    private T data;
    private Date timestamp;
}