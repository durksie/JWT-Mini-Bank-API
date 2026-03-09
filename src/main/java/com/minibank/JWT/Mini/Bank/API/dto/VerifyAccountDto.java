package com.minibank.JWT.Mini.Bank.API.dto;

import lombok.Data;

@Data
public class VerifyAccountDto {
    private boolean valid;
    private String accountNumber;
    private String accountHolderName;
    private String accountType;
    private String message;
}