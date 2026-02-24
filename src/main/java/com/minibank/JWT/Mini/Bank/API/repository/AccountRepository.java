package com.minibank.JWT.Mini.Bank.API.repository;

import com.minibank.JWT.Mini.Bank.API.model.AccountEntity;
import com.minibank.JWT.Mini.Bank.API.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {
    Optional<AccountEntity>findByUser(UserEntity userEntity);
    Optional<AccountEntity>findByAccountNumber(String accountNumber);
}
