package com.example.managementapi.Controller.UserController;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-user")
    ApiResponse<List<User>> getUser(){
//        System.out.println("data: "+userService.getUser());
        ApiResponse apiResponse  = new ApiResponse();
        apiResponse.setData(userService.getUser());
    return apiResponse;
    }
}
