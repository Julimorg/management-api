package com.example.managementapi.Enum.ErrorCode;


public enum ErrorCode {
    USER_EXISTED(1001,"User Existed"),
    USERNAME_INVALID(1002, "Username must be at least 3 characters"),
    USERPASSWORD_INVALID(1003, "UserPassword must be at least 3 characters"),
    USERFIRSTNAME_INVALID(1004, "UserFirstName must be at least 3 characters"),
    USERLASTNAME_INVALID(1005, "UserLastName must be at least 3 characters"),
    EMAIL_INVALID(1006, "Invalid Email Format"),
    PHONE_INVALID(1007, "Invalid Phone Number"),

    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
