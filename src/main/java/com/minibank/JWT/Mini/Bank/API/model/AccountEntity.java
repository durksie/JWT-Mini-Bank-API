package com.minibank.JWT.Mini.Bank.API.model;

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

    @PrePersist
    private void generateAccountNumber(){
        //generate random 10-digit account number
        this.accountNumber="ACC"+ System.currentTimeMillis() % 10000000000L;
    }
}
