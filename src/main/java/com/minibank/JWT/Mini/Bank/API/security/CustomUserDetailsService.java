package com.minibank.JWT.Mini.Bank.API.security;

import com.minibank.JWT.Mini.Bank.API.model.UserEntity;
import com.minibank.JWT.Mini.Bank.API.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);

        UserEntity userEntity= userRepository.findByUsername(username)
                .orElseThrow(()->{
                    logger.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: "+ username);});
        logger.info("User found: {}, role: {}", userEntity.getUsername(), userEntity.getRole());
        logger.info("Stored password hash: {}", userEntity.getPassword());
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().replace("ROLE_",""))
                .build();
    }
}
