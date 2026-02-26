package com.minibank.JWT.Mini.Bank.API.controller;

import com.minibank.JWT.Mini.Bank.API.dto.JwtResponseDto;
import com.minibank.JWT.Mini.Bank.API.dto.LoginDto;
import com.minibank.JWT.Mini.Bank.API.dto.RegisterDto;
import com.minibank.JWT.Mini.Bank.API.enums.AccountType;
import com.minibank.JWT.Mini.Bank.API.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    // ================= GET ACCOUNT TYPES =================
    @GetMapping("/account-types")
    public ResponseEntity<List<Map<String, Object>>> getAccountTypes() {
        List<Map<String, Object>> accountTypes = Arrays.stream(AccountType.values())
                .map(type -> {
                    Map<String, Object> typeInfo = new HashMap<>();
                    typeInfo.put("name", type.name());
                    typeInfo.put("displayName", type.getDisplayName());
                    typeInfo.put("minimumBalance", type.getMinimumBalance());
                    typeInfo.put("maximumBalance", type.getMaximumBalance());
                    return typeInfo;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(accountTypes);
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Validated @RequestBody RegisterDto registerDto) {
        logger.info("Registration attempt for username: {} with account type: {}",
                registerDto.getUsername(), registerDto.getAccountType());

        Map<String, String> response = new HashMap<>();
        try {
            String message = userService.register(registerDto);
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage());
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginDto loginDto) {
        logger.info("Login attempt for username: {}", loginDto.getUsername());

        Map<String, String> errorResponse = new HashMap<>();
        try {
            JwtResponseDto jwtResponse = userService.login(loginDto);
            logger.info("Login successful for username: {}", loginDto.getUsername());
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            logger.error("Login failed for username: {}, error: {}", loginDto.getUsername(), e.getMessage());
            errorResponse.put("error", "Invalid username or password");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}