package com.example.managementapi.Controller;

import com.example.managementapi.Dto.ApiResponse;
import com.example.managementapi.Dto.Email.MailBody;
import com.example.managementapi.Dto.Request.Auth.*;
import com.example.managementapi.Dto.Response.Auth.LoginRes;
import com.example.managementapi.Dto.Response.Auth.IntrospectResponse;
import com.example.managementapi.Dto.Response.Auth.RefreshRes;
import com.example.managementapi.Entity.ForgotPassword;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Repository.ForgotPasswordRepository;
import com.example.managementapi.Repository.UserRepository;
import com.example.managementapi.Service.AuthenticateService;
import com.example.managementapi.Service.EmailService;
import com.example.managementapi.Service.ForgetPassService;
import com.example.managementapi.Service.UserService;
import com.example.managementapi.Util.ChangePassword;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final  UserService userService;

    private final AuthenticateService authenticationService;

    private final UserRepository userRepository;

    private final ForgotPasswordRepository  forgotPasswordRepository;

    private final PasswordEncoder passwordEncoder;

    private final ForgetPassService forgetPassService;


    @PostMapping("/sign-up")
    ApiResponse<User> signUp(@RequestBody @Valid SignUpReq request){

        return ApiResponse.<User>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(userService.signUp(request))
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

    @PostMapping("/verify-email/{email}")
    public ApiResponse<String> verifyEmail(@PathVariable String email) throws Exception {

        forgetPassService.sendOtpViaEmail(email);

        return ApiResponse.<String>builder()
                .code(1000)
                .status_code(HttpStatus.OK.value())
                .message("Send Email Successfully!")
                .build();
    }



    @PostMapping("/verifyOtp/{otp}/{email}")  // --> Có thể làm với Dto
    public ResponseEntity<String> verifyOtp(@PathVariable int otp, @PathVariable String email){
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found!"));

        ForgotPassword fp = forgotPasswordRepository
                .findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        //? check ExpiryDate của OTP
        //? Nếu ExpiryDate là 12:20 mà CurrentTime của User là 12:25
        //? if ( 12:20-(ExpiryDate) before 12:25-(CurrentTime) ) --> True --> OTP dã hết hạn
        if(fp.getExpirationTime().before(Date.from(Instant.now())))
        {
            //? Cần phải delete đi OTP cũ
            //? Vì FP OneToOne với User, nên 1 user ko nên có quá nhiều mã OTP trong DB
            //? Nên cứ Expiry thì del nó đi cho đồng bộ relational
            forgotPasswordRepository.deleteById(fp.getId());
            return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified!");
    }

    @PostMapping("/change-password/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email){

        if(!Objects.equals(changePassword.password(), changePassword.newPassword())){
            return new ResponseEntity<>("Passwords do not match!", HttpStatus.BAD_REQUEST);
        }

        String encoderPasswrod = passwordEncoder.encode(changePassword.password());

        userRepository.updatePassword(email, encoderPasswrod);

        return ResponseEntity.ok("Update successfull!");

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
