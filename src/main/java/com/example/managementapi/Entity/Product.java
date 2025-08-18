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
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String product_id;
    private String product_name;
    private String product_description;
    private String product_image;
    private String product_volume;
    private String product_unit;
    private String product_code;

    private double discount;
    private double product_price;

    private int product_quantity;

    private Date create_at;
    private Date update_at;

}
