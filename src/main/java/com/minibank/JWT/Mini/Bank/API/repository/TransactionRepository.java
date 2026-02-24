package com.minibank.JWT.Mini.Bank.API.repository;

import com.minibank.JWT.Mini.Bank.API.model.AccountEntity;
import com.minibank.JWT.Mini.Bank.API.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {
    List<TransactionEntity>findByAccountOrderByTransactionDateDesc(AccountEntity accountEntity);
}
