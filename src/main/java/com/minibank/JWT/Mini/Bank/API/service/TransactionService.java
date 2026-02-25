package com.minibank.JWT.Mini.Bank.API.service;

import com.minibank.JWT.Mini.Bank.API.dto.TransactionDto;
import com.minibank.JWT.Mini.Bank.API.model.AccountEntity;
import com.minibank.JWT.Mini.Bank.API.model.TransactionEntity;
import com.minibank.JWT.Mini.Bank.API.model.UserEntity;
import com.minibank.JWT.Mini.Bank.API.repository.AccountRepository;
import com.minibank.JWT.Mini.Bank.API.repository.TransactionRepository;
import com.minibank.JWT.Mini.Bank.API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TransactionDto> getUserTransaction(){
        UserEntity userEntity= getCurrentUser();

        AccountEntity accountEntity= accountRepository.findByUser(userEntity)
                .orElseThrow(()->new RuntimeException("Account not found"));

        List<TransactionEntity>transactionEntities=transactionRepository
                .findByAccountOrderByTransactionDateDesc(accountEntity);

        return  transactionEntities.stream()
                .map(TransactionDto::fromEntity)
                .collect(Collectors.toList());
    }

    private UserEntity getCurrentUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()->new RuntimeException("User not found"));
    }

}
