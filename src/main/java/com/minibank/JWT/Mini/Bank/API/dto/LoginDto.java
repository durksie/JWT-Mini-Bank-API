package com.minibank.JWT.Mini.Bank.API.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginDto {
    private  String username;
    private String password;
}
