package com.vitaly.dlmanager.security;
//  17-Feb-24
// gh crazym8nd

import com.vitaly.dlmanager.entity.UserEntity;
import com.vitaly.dlmanager.entity.UserStatus;
import com.vitaly.dlmanager.exception.AuthException;
import com.vitaly.dlmanager.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;


// token generation
@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;

    @Value("${jwt.issuer}")
    private String issuer;

    private TokenDetails generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("username", user.getUsername());
        return generateToken(claims, user.getId().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject){
            Date createdDate = new Date();
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(createdDate)
                    .setId(UUID.randomUUID().toString())
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                    .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }
    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .flatMap(user ->{
                    if(user.getStatus().equals(UserStatus.DELETED)){
                        return Mono.error(new AuthException("User deleted", "USER_IS_DELETED"));
                    }

                    if(!passwordEncoder.matches(password, user.getPassword())){
                        return Mono.error(new AuthException("Ivalid password", "INVALID_PASSWORD"));
                    }

                    return Mono.just(generateToken(user).toBuilder()
                            .userId(user.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new AuthException("User not found", "USER_NOT_FOUND")));
    }
}