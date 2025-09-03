package com.example.managementapi.Dto.Response.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRes {
    private String firstName;
    private String lastName;
    private String userName;

    private String email;
    private String phoneNumber;
    private String role;
    private LocalDate birthDate;
    private String address;

    private LocalDateTime updateAt;

}
