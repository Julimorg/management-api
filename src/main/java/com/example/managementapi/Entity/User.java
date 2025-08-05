package com.example.managementapi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
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
