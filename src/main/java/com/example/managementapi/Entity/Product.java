package com.example.managementapi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private String productVolume;
    private String productUnit;
    private String productCode;

    private int productQuantity;

    private double discount;
    private double productPrice;


    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;

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
