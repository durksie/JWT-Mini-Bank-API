package com.minibank.JWT.Mini.Bank.API.controller;

import com.minibank.JWT.Mini.Bank.API.dto.AccountDto;
import com.minibank.JWT.Mini.Bank.API.dto.TransactionDto;
import com.minibank.JWT.Mini.Bank.API.dto.TransferDto;
import com.minibank.JWT.Mini.Bank.API.dto.VerifyAccountDto;
import com.minibank.JWT.Mini.Bank.API.model.AccountEntity;
import com.minibank.JWT.Mini.Bank.API.service.AccountService;
import com.minibank.JWT.Mini.Bank.API.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<AccountDto> getAccountDetails() {
        try {
            AccountDto accountDto = accountService.getAccountDetails();
            return ResponseEntity.ok(accountDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //To Deposit Money
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam BigDecimal amount) {
        try {
            AccountDto accountDto = accountService.deposit(amount);
            return ResponseEntity.ok(accountDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //To withdraw money
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam BigDecimal amount) {
        try {
            AccountDto accountDto = accountService.withdraw(amount);
            return ResponseEntity.ok(accountDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //To get a transaction history of a user
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        List<TransactionDto> transactionDtos = transactionService.getUserTransaction();
        return ResponseEntity.ok(transactionDtos);
    }
    @GetMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam String accountNumber) {
        try {
            AccountEntity account = accountService.findByAccountNumber(accountNumber);
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("accountNumber", account.getAccountNumber());
            response.put("accountHolder", account.getUser().getUsername());
            response.put("accountType", account.getAccountType().getDisplayName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("valid", "false");
            errorResponse.put("error", "Account not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferDto transferDto) {
        try {
            TransferDto result = accountService.transferMoney(transferDto);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "Transfer completed successfully");
            successResponse.put("amount", result.getAmount());
            successResponse.put("recipient", result.getRecipientName());
            successResponse.put("destinationAccount", result.getDestinationAccountNumber());
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}