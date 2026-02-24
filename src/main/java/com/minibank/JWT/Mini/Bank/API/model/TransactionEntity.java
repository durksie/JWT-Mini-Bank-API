package com.minibank.JWT.Mini.Bank.API.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private  String description;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "account_id",nullable = false)
    private AccountEntity account;

    public enum TransactionType{
        DEPOSIT,WITHDRAWAL,TRANSFER
    }


}
