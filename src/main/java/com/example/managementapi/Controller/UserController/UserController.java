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

//    1. Nguyên tắc chung khi viết method trong Controller
//    Mỗi method CRUD thường tuân theo 5 bước xử lý:
//
//    Nhận request
//
//      Nếu có @PathVariable hoặc @RequestParam → lấy và validate cơ bản.
//
//      Nếu có body (@RequestBody) → validate theo annotation (@Valid, @NotNull, …).
//
//    Gọi service để xử lý logic
//
//      Controller không làm trực tiếp việc truy vấn DB hay xử lý phức tạp.
//
//      Chuyển dữ liệu request sang service.
//
//    Service thực hiện
//
//      Gọi repository.
//
//      Chuyển đổi Entity ↔ DTO.
//
//      Xử lý business logic.
//
//    Nhận kết quả từ service và wrap vào DTO response (ví dụ ResGetUser).
//
//    Trả về Response
//
//      Dùng ApiResponse.builder() hoặc cách khác để format JSON chuẩn.


//    Controller: nhận request → gọi service → nhận DTO → trả response.
//
//    Service: xử lý logic, làm việc với DB qua Repository, convert Entity ↔ DTO.
//
//    Repository: chỉ chứa các method truy vấn DB (JPA).

    @Autowired
    private UserService userService;


    @GetMapping("/get-user")
    ApiResponse<List<ResGetUser>> getUser(){
        //?GET METHOD
        // 1. (Không cần input param vì lấy tất cả)
        //    Nếu có query param thì validate ở đây
        // 2. Gọi service để lấy dữ liệu user từ DB
        // 3. Service sẽ xử lý convert Entity -> ResGetUser
        // 4. Nhận kết quả (list DTO) từ service
        // 5. Trả về response format chuẩn
        return ApiResponse.<List<ResGetUser>>builder()
                .data(userService.getUser())
                .build();
    }

    @PutMapping("/update-user/{userId}")
    ApiResponse<ResUpdateUser> updateUserById(@PathVariable String userId, @RequestBody @Valid ReqUpdateUser request){
        // 1. Nhận userId từ URL + validate request body

        // 2. Gọi service update user

        // 3. Service check user tồn tại, cập nhật DB, convert sang DTO

        // 4. Nhận DTO từ service

        // 5. Trả về response
        return ApiResponse.<ResUpdateUser>builder()
                .data(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/delete-user/{userId}")
    ApiResponse<String> deleteUserById(@PathVariable String userId){

        // 1. Nhận userId từ URL

        // 2. Gọi service để xóa

        // 3. Service check tồn tại và xóa DB

        // 4. Không cần DTO trả về

        // 5. Trả về message

        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .data("User has been deleted")
                .build();
    }

}
