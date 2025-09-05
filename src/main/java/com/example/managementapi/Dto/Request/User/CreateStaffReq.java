package com.example.managementapi.Dto.Request.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateStaffReq {

    @Size(min = 2, message = "USERFIRSTNAME_INVALID")
    private String firstName;
    @Size(min = 2, message = "USERLASTNAME_INVALID")
    private String lastName;
    @Size(min = 2, message = "USERNAME_INVALID")
    private String userName;
    @Size(min = 5, max = 20, message = "USERPASSWORD_INVALID")
    private String password;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",message = "EMAIL_INVALID")
    private String email;
    @Size(min = 2, max = 10, message = "PHONE_INVALID")
    private String phone;
    private LocalDate userDob;
    private String userAddress;
}
