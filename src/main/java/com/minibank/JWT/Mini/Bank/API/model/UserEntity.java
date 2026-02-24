package com.minibank.JWT.Mini.Bank.API.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role="ROLE_USER";//Default role

     //THIS means that one user one account as long user exist
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private AccountEntity account;
}
