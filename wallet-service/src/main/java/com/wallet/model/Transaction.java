package com.wallet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity 
@Table(name="transactions")

public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sourceAccountNumber;

    @Column(nullable = false)
    private String targetAccountNumber;

    @Column(nullable = false)
    private  BigDecimal amount;

    //Record the exact millisecond the teansfer happened
    @Column(nullable = false)
    private LocalDateTime timestamp;

}
