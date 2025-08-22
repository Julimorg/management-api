//package com.example.managementapi.Controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.springdemoapi.Dto.ApiResponse;
//import org.example.springdemoapi.Dto.Request.CreateRoleRequest;
//import org.example.springdemoapi.Dto.Response.RoleResponse;
//import org.example.springdemoapi.Service.RoleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/role")
//@RequiredArgsConstructor
//public class RoleController {
//
//    @Autowired
//    private final RoleService roleService;
//
//    @GetMapping("/get-role")
//    public ApiResponse<List<RoleResponse>> getRole() {
//        return ApiResponse.<List<RoleResponse>>builder()
//                .data(roleService.getRole())
//                .build();
//    }
//
//    @PostMapping("/create-role")
//    public ApiResponse<RoleResponse> createRole(@RequestBody CreateRoleRequest request) {
//        return ApiResponse.<RoleResponse>builder()
//                .data(roleService.createRole(request))
//                .build();
//    }
//
//    @DeleteMapping("/delete-role")
//    public ApiResponse<String> deleteRole(){
//        return ApiResponse.<String>builder()
//                .message("Delete role!")
//                .build();
//    }
//}
