package com.example.managementapi.Dto.Response.User;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserRes {
    private String userId;
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
