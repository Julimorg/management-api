package com.example.managementapi.Dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUseReq {

    @Size(min = 2, message = "USERFIRSTNAME_INVALID")
    private String firstName;
    @Size(min = 2, message = "USERLASTNAME_INVALID")
    private String lastName;
    @Size(min = 2, message = "USERNAME_INVALID")
    private String userName;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",message = "EMAIL_INVALID")
    private String email;
    @Size(min = 2, max = 10, message = "PHONE_INVALID")
    private String phoneNumber;
    private String role;
    private LocalDate birthDate;
    private String address;
    private String isActive;
}
