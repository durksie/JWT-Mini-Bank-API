package com.minibank.JWT.Mini.Bank.API.service;

import com.minibank.JWT.Mini.Bank.API.dto.JwtResponseDto;
import com.minibank.JWT.Mini.Bank.API.dto.LoginDto;
import com.minibank.JWT.Mini.Bank.API.dto.RegisterDto;
import com.minibank.JWT.Mini.Bank.API.model.AccountEntity;
import com.minibank.JWT.Mini.Bank.API.model.UserEntity;
import com.minibank.JWT.Mini.Bank.API.repository.UserRepository;
import com.minibank.JWT.Mini.Bank.API.security.JwtGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
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
        if (userRepository.existsByUsername(registerDto.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        UserEntity userEntity=new UserEntity();
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userEntity.setRole("ROLE_USER");

        //Creates account for user
        AccountEntity accountEntity= new AccountEntity();
        accountEntity.setUser(userEntity);
        userEntity.setAccount(accountEntity);

        userRepository.save(userEntity);

        return "User registered successfully!";
    }

    public JwtResponseDto login(LoginDto loginDto){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new JwtResponseDto(token,loginDto.getUsername());
    }
}
