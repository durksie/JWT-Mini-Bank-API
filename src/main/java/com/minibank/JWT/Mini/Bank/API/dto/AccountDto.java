package com.minibank.JWT.Mini.Bank.API.dto;

import com.minibank.JWT.Mini.Bank.API.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String username;
    private String accountType;
    private String accountTypeDisplayName;
    private double minimumBalance;
    private double maximumBalance;

    public AccountDto(Long id, String accountNumber, BigDecimal balance,
                      String username, AccountType accountType) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.username = username;
        this.accountType = accountType.name();
        this.accountTypeDisplayName = accountType.getDisplayName();
        this.minimumBalance = accountType.getMinimumBalance();
        this.maximumBalance = accountType.getMaximumBalance();
    }


}
