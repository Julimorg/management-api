package com.example.managementapi.Entity;

import jakarta.persistence.*;
import lombok.*;

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

    private int product_quantity;

    private double discount;
    private double product_price;


    private LocalDateTime create_at;
    private LocalDateTime update_at;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier suppliers;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color colors;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
