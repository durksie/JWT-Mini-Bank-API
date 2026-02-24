package com.minibank.JWT.Mini.Bank.API.repository;

import com.minibank.JWT.Mini.Bank.API.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity>findByUsername(String username);
    Boolean existsByUsername(String username);
}
