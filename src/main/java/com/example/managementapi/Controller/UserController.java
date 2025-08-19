package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.UpdateUseReq;
import com.example.managementapi.Dto.Response.GetUserRes;
import com.example.managementapi.Dto.Response.UpdateUserRes;
import com.example.managementapi.Service.UserService;
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
    ApiResponse<List<GetUserRes>> getUser(){
        return ApiResponse.<List<GetUserRes>>builder()
                .data(userService.getUser())
                .build();
    }

    @PutMapping("/update-user/{userId}")
    ApiResponse<UpdateUserRes> updateUserById(@PathVariable String userId, @RequestBody @Valid UpdateUseReq request){
        return ApiResponse.<UpdateUserRes>builder()
                .data(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/delete-user/{userId}")
    ApiResponse<String> deleteUserById(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .data("User has been deleted")
                .build();
    }

}
