package com.minibank.JWT.Mini.Bank.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtMiniBankApiApplication {

	public static void main(String[] args) {

        SpringApplication.run(JwtMiniBankApiApplication.class, args);
        System.out.println("Mini banking app started");
        System.out.println("API EndPoints: ");
        System.out.println("POST /api/auth/register - Register new user");
        System.out.println("   POST /api/auth/login - Login and get JWT token");
        System.out.println("   GET  /api/account - Get account details");
        System.out.println("   POST /api/account/deposit - Deposit money");
        System.out.println("   POST /api/account/withdraw - Withdraw money");
        System.out.println("   GET  /api/account/transactions - View transaction history");

	}

}
