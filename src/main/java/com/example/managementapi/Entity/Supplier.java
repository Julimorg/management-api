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
@Table(name = "Supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String supplier_id;
    private String supplier_name;
    private String supplier_address;
    private String supplier_phone;
    private String supplier_email;
    private String supplier_img;

    private Date create_at;
    private Date update_at;


}
