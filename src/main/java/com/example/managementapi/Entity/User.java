package com.example.managementapi.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
    private String lastName;
    @NotNull
    @Column(unique = true)
    private String userName;
    private String password;
    @NotNull
    @Email
    @Column(unique = true)
    private String email;
    private String phone;
    private String isActive;
    private String user_img;
    private String user_address;

    private LocalDate user_dob;

    private LocalDateTime update_at;
    private LocalDateTime create_at;



    @ManyToMany
    private List<Role> userRoles;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;


}
