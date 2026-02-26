package com.minibank.JWT.Mini.Bank.API.service;

import com.minibank.JWT.Mini.Bank.API.dto.JwtResponseDto;
import com.minibank.JWT.Mini.Bank.API.dto.LoginDto;
import com.minibank.JWT.Mini.Bank.API.dto.RegisterDto;
import com.minibank.JWT.Mini.Bank.API.enums.AccountType;
import com.minibank.JWT.Mini.Bank.API.model.AccountEntity;
import com.minibank.JWT.Mini.Bank.API.model.UserEntity;
import com.minibank.JWT.Mini.Bank.API.repository.UserRepository;
import com.minibank.JWT.Mini.Bank.API.security.JwtGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Transactional
    public String register(RegisterDto registerDto){

        logger.info("Registering new user: {}", registerDto.getUsername());

        if (userRepository.existsByUsername(registerDto.getUsername())){
            logger.warn("Username already exists: {}", registerDto.getUsername());
            throw new RuntimeException("Username already exists");
        }

        // Parse account type
        AccountType accountType;
        try {
            accountType = AccountType.fromDisplayName(registerDto.getAccountType());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Invalid account type. Please choose from: " +
                            "Easy Account, Aspire Account, Premier Account, " +
                            "Private Clients Account, Private Wealth Account"
            );
        }

        // Create user
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userEntity.setRole("ROLE_USER");

        // Create account
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountType(accountType);
        accountEntity.setUser(userEntity);

        // If using OneToOne
        userEntity.setAccount(accountEntity);

        userRepository.save(userEntity);

        logger.info("User registered successfully: {}", registerDto.getUsername());
        logger.info("Account created: Type={}, AccountNumber={}",
                accountType.getDisplayName(),
                accountEntity.getAccountNumber());

        return "User registered successfully! Account Type: "
                + accountType.getDisplayName()
                + " | Account Number: "
                + accountEntity.getAccountNumber();
    }

    public JwtResponseDto login(LoginDto loginDto){
        logger.info("Processing login for user: {}", loginDto.getUsername());

        try {
            // First, let's verify the user exists and password matches manually for debugging
            UserEntity userEntity = userRepository.findByUsername(loginDto.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            logger.info("Found user: {}", userEntity.getUsername());
            logger.info("Stored password hash: {}", userEntity.getPassword());

            boolean passwordMatches = passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword());
            logger.info("Password matches: {}", passwordMatches);

            if (!passwordMatches) {
                throw new BadCredentialsException("Invalid password");
            }

            // If manual verification passes, proceed with authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            logger.info("Login successful for user: {}", loginDto.getUsername());

            return new JwtResponseDto(token, loginDto.getUsername());

        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user {}: {}", loginDto.getUsername(), e.getMessage());
            throw new RuntimeException("Invalid username or password");
        } catch (Exception e) {
            logger.error("Unexpected error during login for user {}: {}", loginDto.getUsername(), e.getMessage());
            throw new RuntimeException("Invalid username or password");
        }
    }
}
