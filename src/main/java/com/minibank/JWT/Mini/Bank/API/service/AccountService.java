package com.minibank.JWT.Mini.Bank.API.service;

import com.minibank.JWT.Mini.Bank.API.dto.AccountDto;
import com.minibank.JWT.Mini.Bank.API.model.AccountEntity;
import com.minibank.JWT.Mini.Bank.API.model.TransactionEntity;
import com.minibank.JWT.Mini.Bank.API.model.UserEntity;
import com.minibank.JWT.Mini.Bank.API.repository.AccountRepository;
import com.minibank.JWT.Mini.Bank.API.repository.TransactionRepository;
import com.minibank.JWT.Mini.Bank.API.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public AccountDto getAccountDetails(){
        UserEntity userEntity=getCurrentUser();
        AccountEntity accountEntity= accountRepository.findByUser(userEntity)
                .orElseThrow(()->new RuntimeException("Account not found"));
        return new AccountDto(
                accountEntity.getId(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                userEntity.getUsername()
        );
    }
    @Transactional
    public  AccountDto deposit(BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO)<=0){
            throw new RuntimeException("Amount must be positive");
        }
        UserEntity userEntity=getCurrentUser();

        AccountEntity accountEntity = accountRepository.findByUser(userEntity).orElseThrow(()->new RuntimeException("Account not found"));

        accountEntity.setBalance(accountEntity.getBalance().add(amount));
        accountRepository.save(accountEntity);

        TransactionEntity transactionEntity= new TransactionEntity();
        transactionEntity.setAmount(amount);
        transactionEntity.setType(TransactionEntity.TransactionType.DEPOSIT);
        transactionEntity.setDescription("Deposit to account");
        transactionEntity.setAccount(accountEntity);
        transactionRepository.save(transactionEntity);

        return new AccountDto(
               accountEntity.getId(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                userEntity.getUsername()
        );
    }
    @Transactional
    public AccountDto withdraw(BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO)<=0){
            throw new RuntimeException("Amount must be positive");
        }

        UserEntity userEntity=getCurrentUser();

        AccountEntity accountEntity= accountRepository.findByUser(userEntity)
                .orElseThrow(()->new RuntimeException("Account not found"));

        if (accountEntity.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient balance");
        }
        accountEntity.setBalance(accountEntity.getBalance().subtract(amount));
        accountRepository.save(accountEntity);

        //create transaction record
        TransactionEntity transactionEntity= new TransactionEntity();
        transactionEntity.setAmount(amount);
        transactionEntity.setType(TransactionEntity.TransactionType.WITHDRAWAL);
        transactionEntity.setDescription("withdrawal from account");
        transactionEntity.setAccount(accountEntity);
        transactionRepository.save(transactionEntity);

        return new AccountDto(
                accountEntity.getId(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                userEntity.getUsername()
        );
    }

    private UserEntity getCurrentUser(){
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()->new RuntimeException("User not found"));
    }
}
