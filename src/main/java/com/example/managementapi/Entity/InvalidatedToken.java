package com.example.managementapi.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "InvalidatedToken")
public class InvalidatedToken {
    @Id
    private String id;
    private Date expirDate;
}
