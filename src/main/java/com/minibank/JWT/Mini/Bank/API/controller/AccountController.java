package com.minibank.JWT.Mini.Bank.API.controller;

import com.minibank.JWT.Mini.Bank.API.dto.AccountDto;
import com.minibank.JWT.Mini.Bank.API.dto.TransactionDto;
import com.minibank.JWT.Mini.Bank.API.service.AccountService;
import com.minibank.JWT.Mini.Bank.API.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<AccountDto> getAccountDetails(){
        try{
            AccountDto accountDto = accountService.getAccountDetails();
            return  ResponseEntity.ok(accountDto);
        }catch (RuntimeException e){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?>deposit(@RequestParam BigDecimal amount){
        try{
            AccountDto accountDto= accountService.deposit(amount);
            return ResponseEntity.ok(accountDto);
        }catch (RuntimeException e){
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?>withdraw(@RequestParam BigDecimal amount){
        try{
            AccountDto accountDto= accountService.withdraw(amount);
            return ResponseEntity.ok(accountDto);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>>getTransactions(){
        List<TransactionDto> transactionDtos= transactionService.getUserTransaction();
        return  ResponseEntity.ok(transactionDtos);
    }
}
