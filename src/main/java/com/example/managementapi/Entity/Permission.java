package com.example.managementapi.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Permission")
public class Permission {
    @Id
    @Column(unique = true)
    private String permission_name;
    private String permission_description;

    private LocalDateTime create_at;
    private LocalDateTime update_at;

    @ManyToMany
    private List<Role> roles;
}
