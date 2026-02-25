package com.minibank.JWT.Mini.Bank.API.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
    private String type= "Bearer";
    private String username;

    public JwtResponseDto(String token,String username){
        this.token=token;
        this.username=username;
    }
}
