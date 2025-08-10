package com.example.managementapi.Controller.AuthController;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Request.AuthenticationRequest;
import com.example.managementapi.Dto.Request.IntrospectRequest;
import com.example.managementapi.Dto.Request.ReqCreateUser;
import com.example.managementapi.Dto.Response.AuthenticationResponse;
import com.example.managementapi.Dto.Response.IntrospectResponse;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Service.AuthenticateService.AuthenticateService;
import com.example.managementapi.Service.UserService.UserService;
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

    @PostMapping("/sign-up")
    ApiResponse<User> signUp(@RequestBody @Valid ReqCreateUser request){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
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
