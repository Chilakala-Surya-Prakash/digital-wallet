package com.wallet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data // shortcut from lombok tool which automatically generates all the b
        //boilerplate code like getBalance(), setBaalance() hidden in the bg
@Entity
@Table(name = "accounts")
public class Account{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable = false)
    private String accountNumber;

    @Column(nullable=false)
    private BigDecimal balance;
}