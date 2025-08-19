package com.example.managementapi.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Role")
public class Role {
    @Id
    @Column(unique = true)
    private String role_name;
    private String role_description;

    private LocalDateTime update_at;
    private LocalDateTime create_at;

    @ManyToMany
    private List<Permission> permissions;
}
