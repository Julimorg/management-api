package com.example.managementapi.Dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ResCreateUser {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private String role;
    private LocalDate birthDate;
    private String address;
    private String isActive;
}
