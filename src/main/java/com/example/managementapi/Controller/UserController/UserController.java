package com.example.managementapi.Controller.UserController;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.ReqCreateUser;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Service.UserService.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-user")
    ApiResponse<List<User>> getUser(){
        ApiResponse apiResponse  = new ApiResponse();
        apiResponse.setData(userService.getUser());
    return apiResponse;
    }

    @PostMapping("/sign-up")
    ApiResponse<User> signUp(@RequestBody @Valid ReqCreateUser request){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }
}
