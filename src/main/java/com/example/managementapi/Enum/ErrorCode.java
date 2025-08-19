package com.example.managementapi.Enum;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED(1001,"User Existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USERPASSWORD_INVALID(1003, "UserPassword must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USERFIRSTNAME_INVALID(1004, "UserFirstName must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USERLASTNAME_INVALID(1005, "UserLastName must be at least 3 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1006, "Invalid Email Format", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1007, "Invalid Phone Number", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED (1008, "User Not Existed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICAED(1009, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1010, "Unauthorized", HttpStatus.UNAUTHORIZED),
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
