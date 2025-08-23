package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Role.CreateRoleReq;
import com.example.managementapi.Dto.Response.Role.CreateRoleRes;
import com.example.managementapi.Dto.Response.Role.RoleRes;
import com.example.managementapi.Service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/role")
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    private final RoleService roleService;

    @GetMapping("/get-role")
    public ApiResponse<List<RoleRes>> getRole() {
        return ApiResponse.<List<RoleRes>>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleService.getRole())
                .build();
    }

    @PostMapping("/create-role")
    public ApiResponse<CreateRoleRes> createRole(@RequestBody CreateRoleReq request) {
        return ApiResponse.<CreateRoleRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleService.createRole(request))
                .build();
    }

    @DeleteMapping("/delete-role/{roleName}")
    public ApiResponse<String> deleteRole(@PathVariable String roleName){
        roleService.deleteRole(roleName);
        return ApiResponse.<String>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .message("Delete role!")
                .build();
    }
}
