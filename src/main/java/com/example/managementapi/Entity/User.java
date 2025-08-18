package com.example.managementapi.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String user_id;
    private String firstName;
    private String last_name;
    private String user_name;
    private String user_password;
    private String user_email;
    private String user_phoneNumber;
    private String user_isActive;
    private String user_img;
    private String user_address;


    private Date user_dob;
    private Date update_at;
    private Date create_at;



    @ManyToMany
    Set<Role> userRoles;


}
