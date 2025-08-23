package com.example.managementapi.Controller;


import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Color.CreateColorReq;
import com.example.managementapi.Dto.Request.Color.UpdateColorReq;
import com.example.managementapi.Dto.Response.Color.CreateColorRes;
import com.example.managementapi.Dto.Response.Color.GetColorDetailRes;
import com.example.managementapi.Dto.Response.Color.GetColorRes;
import com.example.managementapi.Dto.Response.Color.UpdateColorRes;
import com.example.managementapi.Service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/color")
public class ColorController {
    @Autowired
    private ColorService colorService;

    @GetMapping("/get-color")
    public ApiResponse<List<GetColorRes>> getColor(){
        return ApiResponse.<List<GetColorRes>>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(colorService.getColor())
                .build();
    }

    @GetMapping("/detail-color/{colorId}")
    public ApiResponse<GetColorDetailRes> getColorById(@PathVariable String colorId){
        return ApiResponse.<GetColorDetailRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(colorService.getColorDetail(colorId))
                .build();
    }

    @PostMapping(value = "/create-color", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CreateColorRes> createColor(
            @RequestPart("colorName") String colorName,
            @RequestPart(value = "colorCode", required = false) String colorCode,
            @RequestPart(value = "colorDescription", required = false) String colorDescription,
            @RequestPart(value = "colorImg", required = false) MultipartFile colorImg) {

        CreateColorReq request = CreateColorReq.builder()
                .colorName(colorName)
                .colorCode(colorCode)
                .colorDescription(colorDescription)
                .colorImg(colorImg)
                .build();

        return ApiResponse.<CreateColorRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(colorService.createColor(request))
                .build();
    }

    @PutMapping("/edit-color/{colorId}")
    public ApiResponse<UpdateColorRes> updateColor(@PathVariable String colorId, @RequestBody UpdateColorReq request) {
        return ApiResponse.<UpdateColorRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(colorService.updateColor(colorId, request))
                .build();
    }

    @DeleteMapping("/delete-color/{colorId}")
    public ApiResponse<String> deleteColor(@PathVariable String colorId) {
        colorService.deleteColor(colorId);
        return ApiResponse.<String>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message("Delete Color Successfully!")
                .build();
    }

}
