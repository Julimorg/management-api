package com.example.managementapi.Controller.AuthController;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.ReqCreateUser;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Service.UserService.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    ApiResponse<User> signUp(@RequestBody @Valid ReqCreateUser request){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }


}
