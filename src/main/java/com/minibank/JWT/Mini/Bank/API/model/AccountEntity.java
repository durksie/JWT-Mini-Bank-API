package com.minibank.JWT.Mini.Bank.API.model;

import com.minibank.JWT.Mini.Bank.API.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance= BigDecimal.ZERO;//Initializes the balance

    @OneToOne
    @JoinColumn(name = "user_id",unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<TransactionEntity>transactionEntities= new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @PrePersist
    private void generateAccountNumber() {

        if (this.accountNumber != null) return;

        String prefix = getAccountTypePrefix();

        this.accountNumber =
                prefix +
                        System.currentTimeMillis() +
                        String.format("%03d", (int)(Math.random() * 1000));
    }

    private String getAccountTypePrefix() {
        return switch (accountType) {
            case EASY -> "EASY";
            case ASPIRE -> "ASP";
            case PREMIER -> "PRM";
            case PRIVATE_CLIENTS -> "PCL";
            case PRIVATE_WEALTH -> "PWL";
        };
}}

