package com.example.managementapi.Controller.UserController;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.ReqCreateUser;
import com.example.managementapi.Dto.Request.ReqUpdateUser;
import com.example.managementapi.Dto.Response.ResGetUser;
import com.example.managementapi.Dto.Response.ResUpdateUser;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Mapper.UserMapper.UserMapper;
import com.example.managementapi.Repository.UserRepository;
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
    ApiResponse<List<ResGetUser>> getUser(){
        return ApiResponse.<List<ResGetUser>>builder()
                .data(userService.getUser())
                .build();
    }

    @PutMapping("/update-user")
    ApiResponse<ResUpdateUser> updateUserById(@PathVariable String userId, @RequestBody @Valid ReqUpdateUser request){
        return ApiResponse.<ResUpdateUser>builder()
                .data(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/delete-user")
    ApiResponse<String> deleteUserById(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .data("User has been deleted")
                .build();
    }

}
