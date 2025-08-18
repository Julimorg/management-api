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
@Table(name = "Order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String order_id;
    private String order_code;
    private String order_status;

    private double order_amount;


    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
    private Date complete_at;
}
