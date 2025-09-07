package com.example.managementapi.Enum;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    //* ======================= AUTH CATCH ERROR 5000 =======================
    UNAUTHENTICATED(5000, "UnAuthenticated! Please Log in to continue!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(5001, "Unauthorized", HttpStatus.UNAUTHORIZED),
    BANNED(5002, "You have been banned! ", HttpStatus.FORBIDDEN),

    //* ======================= USER CATCH ERROR 1100 =======================

    USER_EXISTED(1101,"User Existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1102, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_INVALID(1103, "UserPassword must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USER_FIRSTNAME_INVALID(1104, "UserFirstName must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USER_LASTNAME_INVALID(1105, "UserLastName must be at least 3 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1106, "Invalid Email Format", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1107, "Invalid Phone Number", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED (1108, "User Not Existed", HttpStatus.BAD_REQUEST),

    //* ======================= PRODUCT CATCH ERROR 1200 =======================

    PRODUCT_EXISTED(1200,"Product Existed", HttpStatus.BAD_REQUEST),

    //* ======================= SUPPLIER CATCH ERROR 1300 =======================
    SUPPLIER_NOT_EXISTED(1300, "Supplier Not Existed", HttpStatus.BAD_REQUEST),

    //* ======================= COLOR CATCH ERROR 1400 =======================
    COLOR_NOT_EXISTED(1400, "Color Not Existed", HttpStatus.BAD_REQUEST),

    //* ======================= CATEGORY CATCH ERROR 1500 =======================

    CATEGORY_EXISTED(1501, "Category Existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1502, "Category Not Existed", HttpStatus.BAD_REQUEST),

    //* ======================= CLOUDINARY CATCH ERROR 3000 =======================
    IMG_OVER_SIZE(3000, "Your Image is over size!", HttpStatus.BAD_REQUEST),

    //* ======================= UNKNOWN CATCH ERROR 9000 =======================

    UNKNOWN_ERROR(9000, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),
    WRONG_PATH(9001, "Wrong Path", HttpStatus.BAD_REQUEST),

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
