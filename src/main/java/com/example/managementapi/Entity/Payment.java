package com.example.managementapi.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String payment_id;
    private String payment_method;
    private String payment_status;
    private String response_code;
    private String transaction_status;
    private String transaction_id;
    private String bank_code;
    private String bank_transaction_no;
    private String card_type;
    private String card_number;
    private String txn_type;
    private String order_info;
    private String secure_hash;

    private double amount;

    private LocalDateTime pay_date;
    private LocalDateTime create_at;
    private LocalDateTime update_at;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
