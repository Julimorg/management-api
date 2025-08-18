package com.example.managementapi.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String color_id;

    private String color_name;
    private String color_code;
    private String color_description;

    private Date created_at;
    private Date updated_at;

}
