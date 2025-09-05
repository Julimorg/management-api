package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.Auth.*;
import com.example.managementapi.Dto.Response.Auth.LoginRes;
import com.example.managementapi.Dto.Response.Auth.IntrospectResponse;
import com.example.managementapi.Dto.Response.Auth.RefreshRes;
import com.example.managementapi.Dto.Response.User.SignUpUserRes;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Service.AuthenticateService;
import com.example.managementapi.Service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateService authenticationService;

    @PostMapping("/sign-up")
    ApiResponse<SignUpUserRes> signUp(@RequestBody @Valid SignUpReq request){

        return ApiResponse.<SignUpUserRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(authenticationService.signUp(request))
                .build();
    }

    @PostMapping("/log-in")
    ApiResponse<LoginRes> login(@RequestBody LoginReq request){
        var result = authenticationService.login(request);

        return ApiResponse.<LoginRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(result)
                .build();
    }


//    @PreAuthorize("")
    @PostMapping("/log-out")
    ApiResponse<String> logout(@RequestBody LogOutReq request)
            throws ParseException, JOSEException {

        authenticationService.logOut(request);
        return ApiResponse.<String>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .message("Log out successfully!")
                .build();
    }


    @PostMapping("/refresh-token")
    ApiResponse<RefreshRes> refreshToken(@RequestBody RefreshReq request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);

        return ApiResponse.<RefreshRes>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(result)
                .build();
    }


    @PostMapping("/introspect-token")
    ApiResponse<IntrospectResponse> checkVerifyToken(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(result)
                .build();
    }


}
