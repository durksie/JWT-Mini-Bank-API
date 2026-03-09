package com.minibank.JWT.Mini.Bank.API.service;

import com.minibank.JWT.Mini.Bank.API.dto.AccountDto;
import com.minibank.JWT.Mini.Bank.API.dto.TransferDto;
import com.minibank.JWT.Mini.Bank.API.dto.VerifyAccountDto;
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


    //  GET ACCOUNT

    public AccountDto getAccountDetails() {

        UserEntity user = getCurrentUser();

        AccountEntity account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        logger.info("Retrieved account for user {} type {}",
                user.getUsername(),
                account.getAccountType().getDisplayName());

        return mapToDto(account);
    }


    //  DEPOSIT
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


    //  WITHDRAW
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

    //HELPERS
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

    /**
     * VERIFY ACCOUNT BEFORE TRANSFER - Step 1
     * Check if destination account exists and return details
     */
    public VerifyAccountDto verifyDestinationAccount(String accountNumber) {
        logger.info("Verifying destination account: {}", accountNumber);

        // Don't allow verifying own account
        UserEntity currentUser = getCurrentUser();
        AccountEntity currentAccount = accountRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Your account not found"));

        if (currentAccount.getAccountNumber().equals(accountNumber)) {
            throw new RuntimeException("Cannot transfer to your own account");
        }

        // Find destination account
        AccountEntity destinationAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        // Create verification response
        VerifyAccountDto verification = new VerifyAccountDto();
        verification.setValid(true);
        verification.setAccountNumber(destinationAccount.getAccountNumber());
        verification.setAccountHolderName(destinationAccount.getUser().getUsername());
        verification.setAccountType(destinationAccount.getAccountType().getDisplayName());

        logger.info("Account verified successfully: {} - {}",
                destinationAccount.getAccountNumber(),
                destinationAccount.getUser().getUsername());

        return verification;
    }

    /**
     * EXECUTE TRANSFER - Step 2 (after verification)
     * Only proceed if account has been verified
     */
    @Transactional
    public TransferDto transferMoney(TransferDto transferDto) {
        logger.info("Processing transfer request to account: {}", transferDto.getDestinationAccountNumber());

        // Validate amount
        if (transferDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        // Get source account (current user)
        UserEntity sourceUser = getCurrentUser();
        AccountEntity sourceAccount = accountRepository.findByUser(sourceUser)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        // Check if source has sufficient balance
        if (sourceAccount.getBalance().compareTo(transferDto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance for transfer");
        }

        // Get destination account (already verified, but double-check)
        AccountEntity destinationAccount = accountRepository.findByAccountNumber(transferDto.getDestinationAccountNumber())
                .orElseThrow(() -> new RuntimeException("Destination account not found. Please verify the account first."));

        // Prevent transferring to own account
        if (sourceAccount.getAccountNumber().equals(destinationAccount.getAccountNumber())) {
            throw new RuntimeException("Cannot transfer money to your own account");
        }

        // Check if destination account type can receive the amount
        BigDecimal destinationNewBalance = destinationAccount.getBalance().add(transferDto.getAmount());
        if (destinationNewBalance.compareTo(BigDecimal.valueOf(destinationAccount.getAccountType().getMaximumBalance())) > 0) {
            throw new RuntimeException(String.format(
                    "Transfer would exceed maximum balance limit of %.2f for recipient's %s",
                    destinationAccount.getAccountType().getMaximumBalance(),
                    destinationAccount.getAccountType().getDisplayName()));
        }

        // Check source account minimum balance after transfer
        BigDecimal sourceNewBalance = sourceAccount.getBalance().subtract(transferDto.getAmount());
        if (sourceNewBalance.compareTo(BigDecimal.valueOf(sourceAccount.getAccountType().getMinimumBalance())) < 0) {
            throw new RuntimeException(String.format(
                    "Transfer would bring your balance below minimum requirement of %.2f for %s",
                    sourceAccount.getAccountType().getMinimumBalance(),
                    sourceAccount.getAccountType().getDisplayName()));
        }

        // Perform transfer (atomic operation)
        try {
            // Deduct from source
            sourceAccount.setBalance(sourceNewBalance);
            accountRepository.save(sourceAccount);

            // Add to destination
            destinationAccount.setBalance(destinationNewBalance);
            accountRepository.save(destinationAccount);

            // Create transaction record for source (SENT)
            TransactionEntity sourceTransaction = new TransactionEntity();
            sourceTransaction.setAmount(transferDto.getAmount());
            sourceTransaction.setType(TransactionEntity.TransactionType.TRANSFER);
            sourceTransaction.setDescription(transferDto.getDescription() != null ?
                    transferDto.getDescription() : String.format("Transfer to %s", transferDto.getRecipientName()));
            sourceTransaction.setAccount(sourceAccount);
            sourceTransaction.setSourceAccountNumber(sourceAccount.getAccountNumber());
            sourceTransaction.setDestinationAccountNumber(destinationAccount.getAccountNumber());
            sourceTransaction.setRecipientName(transferDto.getRecipientName());
            transactionRepository.save(sourceTransaction);

            // Create transaction record for destination (RECEIVED)
            TransactionEntity destTransaction = new TransactionEntity();
            destTransaction.setAmount(transferDto.getAmount());
            destTransaction.setType(TransactionEntity.TransactionType.TRANSFER);
            destTransaction.setDescription(String.format("Transfer from %s", sourceUser.getUsername()));
            destTransaction.setAccount(destinationAccount);
            destTransaction.setSourceAccountNumber(sourceAccount.getAccountNumber());
            destTransaction.setDestinationAccountNumber(destinationAccount.getAccountNumber());
            destTransaction.setRecipientName(destinationAccount.getUser().getUsername());
            transactionRepository.save(destTransaction);

            logger.info("Transfer of {} from account {} to {} completed successfully",
                    transferDto.getAmount(), sourceAccount.getAccountNumber(),
                    destinationAccount.getAccountNumber());

            // Set response fields
            transferDto.setSourceAccountNumber(sourceAccount.getAccountNumber());
            transferDto.setStatus("COMPLETED");
            transferDto.setSourceNewBalance(sourceAccount.getBalance());
            transferDto.setDestinationNewBalance(destinationAccount.getBalance());

        } catch (Exception e) {
            logger.error("Transfer failed: {}", e.getMessage());
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }

        return transferDto;
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

    public AccountEntity findByAccountNumber(String accountNumber) {

        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}