package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.User.UpdateUseReq;
import com.example.managementapi.Dto.Response.User.GetUserRes;
import com.example.managementapi.Dto.Response.User.SearchUserRes;
import com.example.managementapi.Dto.Response.User.UpdateUserRes;
import com.example.managementapi.Dto.Response.User.UserSearchResByAdmin;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Slf4j
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

// ** =============================== ROLE USER ===============================

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


    @GetMapping("/search-user")
    public Page<UserSearchResByAdmin> searchUser(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status" , required = false) String status,
            //? Đây là những Page default nếu không truyền trên url
            //? ví dụ GET /api/v1/users/search-user
            //?          page = 0 (default 0-based)
            //?          size = 10
            //?          sort = createAt, asc
            @PageableDefault(size = 10, sort = "createAt", direction = Sort.Direction.ASC) Pageable pageable){
                return userService.searchUserByAdmin(keyword, status, pageable);
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

// ** =============================== ROLE ADMIN ===============================

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
