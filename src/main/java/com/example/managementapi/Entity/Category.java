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
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String category_id;
    private String category_name;
    private String category_description;
    private String category_image;

    private Date create_at;
    private Date update_at;
}
