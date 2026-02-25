package com.minibank.JWT.Mini.Bank.API.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtGenerator {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(JwtGenerator.class);

    //@Value("${jwt.secret}")
    private String jwtSecret;

    //@Value("${jwt.expiration}")
    private int jwtExpiration;

    //converts the secret key into cryptographic object that Java's security libraries can actually use to sign or verify tokens.
    private Key key(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(Authentication authentication){
        String username= authentication.getName();
        Date currentDate=new Date();
        Date expireDate= new Date(currentDate.getTime()+jwtExpiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }
    public String getUsernameFromJwt(String token){
        Claims claims=Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return  true;
        }catch(Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT HAS EXPIRED OR INCORRECT");
        }
    }
}
