package com.example.managementapi.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResUpdateUser {
    private String firstName;
    private String lastName;
    private String userName;
    //    private String password;
    private String email;
    private String phoneNumber;
    private String role;
    private LocalDate birthDate;
    private String address;
    private String isActive;
}
