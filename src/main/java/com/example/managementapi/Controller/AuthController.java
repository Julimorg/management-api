package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.*;
import com.example.managementapi.Dto.Response.LoginRes;
import com.example.managementapi.Dto.Response.IntrospectResponse;
import com.example.managementapi.Dto.Response.RefreshRes;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Repository.UserRepository;
import com.example.managementapi.Service.AuthenticateService;
import com.example.managementapi.Service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;

    private final AuthenticateService authenticationService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/sign-up")
    ApiResponse<User> signUp(@RequestBody @Valid SignUpReq request){

        return ApiResponse.<User>builder()
                .data(userService.createUser(request))
                .build();
    }

    @PostMapping("/log-in")
    ApiResponse<LoginRes> authenticate(@RequestBody LoginReq request){
        var result = authenticationService.login(request);

        return ApiResponse.<LoginRes>builder()
                .data(result)
                .build();
    }

    @PostMapping("/log-out")
    ApiResponse<String> logout(@RequestBody LogOutReq request)
            throws ParseException, JOSEException {

        authenticationService.logOut(request);
        return ApiResponse.<String>builder()
                .message("Log out successfully!")
                .build();
    }

    @PostMapping("/refresh-token")
    ApiResponse<RefreshRes> refreshToken(@RequestBody RefreshReq request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);

        return ApiResponse.<RefreshRes>builder()
                .data(result)
                .build();
    }


    @PostMapping("/introspect-token")
    ApiResponse<IntrospectResponse> checkVerifyToken(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }


}
