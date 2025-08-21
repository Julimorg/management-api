package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.UpdateUseReq;
import com.example.managementapi.Dto.Response.GetUserRes;
import com.example.managementapi.Dto.Response.UpdateUserRes;
import com.example.managementapi.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Slf4j
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/get-user")
    ApiResponse<List<GetUserRes>> getUser(){
        log.warn(String.valueOf(HttpStatus.OK));
        return ApiResponse.<List<GetUserRes>>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(userService.getUser())
                .build();
    }

    @PutMapping("/update-user/{userId}")
    ApiResponse<UpdateUserRes> updateUserById(@PathVariable String userId, @RequestBody @Valid UpdateUseReq request){
        return ApiResponse.<UpdateUserRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/delete-user/{userId}")
    ApiResponse<String> deleteUserById(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("User has been deleted")
                .build();
    }

}
