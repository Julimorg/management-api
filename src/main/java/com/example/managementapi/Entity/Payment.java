package com.example.managementapi.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private String paymentId;
    private String paymentMethod;
    private String paymentStatus;
    private String responseCode;
    private String transactionStatus;
    private String transactionId;
    private String bankCode;
    private String bankTransactionNo;
    private String cardType;
    private String cardNumber;
    private String txnType;
    private String orderInfo;
    private String secureHash;

    private double amount;

    private LocalDateTime payDate;
    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
