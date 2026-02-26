package com.minibank.JWT.Mini.Bank.API.service;

import com.minibank.JWT.Mini.Bank.API.dto.AccountDto;
import com.minibank.JWT.Mini.Bank.API.model.AccountEntity;
import com.minibank.JWT.Mini.Bank.API.model.TransactionEntity;
import com.minibank.JWT.Mini.Bank.API.model.UserEntity;
import com.minibank.JWT.Mini.Bank.API.repository.AccountRepository;
import com.minibank.JWT.Mini.Bank.API.repository.TransactionRepository;
import com.minibank.JWT.Mini.Bank.API.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final Logger logger =
            LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;


    // ================= GET ACCOUNT =================

    public AccountDto getAccountDetails() {

        UserEntity user = getCurrentUser();

        AccountEntity account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        logger.info("Retrieved account for user {} type {}",
                user.getUsername(),
                account.getAccountType().getDisplayName());

        return mapToDto(account);
    }


    // ================= DEPOSIT =================

    @Transactional
    public AccountDto deposit(BigDecimal amount){

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Amount must be positive");

        UserEntity user = getCurrentUser();

        AccountEntity account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal newBalance = account.getBalance().add(amount);

        // check max balance rule
        if (newBalance.compareTo(
                BigDecimal.valueOf(account.getAccountType().getMaximumBalance())) > 0)
        {
            throw new RuntimeException(
                    "Deposit exceeds maximum balance for "
                            + account.getAccountType().getDisplayName());
        }

        account.setBalance(newBalance);
        accountRepository.save(account);

        saveTransaction(account, amount,
                TransactionEntity.TransactionType.DEPOSIT,
                "Deposit");

        logger.info("Deposit successful {} → new balance {}",
                amount, account.getBalance());

        return mapToDto(account);
    }


    // ================= WITHDRAW =================

    @Transactional
    public AccountDto withdraw(BigDecimal amount){

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Amount must be positive");

        UserEntity user = getCurrentUser();

        AccountEntity account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");

        BigDecimal newBalance = account.getBalance().subtract(amount);

        // check minimum balance rule
        if (newBalance.compareTo(
                BigDecimal.valueOf(account.getAccountType().getMinimumBalance())) < 0)
        {
            throw new RuntimeException(
                    "Withdrawal would go below minimum balance for "
                            + account.getAccountType().getDisplayName());
        }

        account.setBalance(newBalance);
        accountRepository.save(account);

        saveTransaction(account, amount,
                TransactionEntity.TransactionType.WITHDRAWAL,
                "Withdrawal");

        logger.info("Withdrawal successful {} → new balance {}",
                amount, account.getBalance());

        return mapToDto(account);
    }



    // ================= HELPERS =================

    private void saveTransaction(AccountEntity account,
                                 BigDecimal amount,
                                 TransactionEntity.TransactionType type,
                                 String description){

        TransactionEntity transaction = new TransactionEntity();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);

        transactionRepository.save(transaction);
    }


    private AccountDto mapToDto(AccountEntity account){

        var type = account.getAccountType();

        return new AccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getUser().getUsername(),
                type.name(),
                type.getDisplayName(),
                type.getMinimumBalance(),
                type.getMaximumBalance()
        );
    }


    private UserEntity getCurrentUser(){

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}