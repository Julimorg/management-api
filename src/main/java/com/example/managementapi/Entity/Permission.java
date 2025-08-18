package com.example.managementapi.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Permission")
public class Permission {
    @Id
    private String permission_name;
    private String permission_description;
}
