package com.minibank.JWT.Mini.Bank.API.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;

    private String password;

    private String accountType;
}
