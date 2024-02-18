package com.vitaly.dlmanager.security;
//  17-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.exception.AuthException;
import com.vitaly.dlmanager.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

// validation of token
public class JwtHandler {

    private final String secret;

    public JwtHandler(String secret) {
        this.secret = secret;
    }

    public Mono<VerificationResult> verify(String accessToken){
        return  Mono.just(verificationResult(accessToken))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    private VerificationResult verificationResult(String token){
        Claims claims = getClaimsFromToken(token);
        final Date expiration = claims.getExpiration();

        if(expiration.before(new Date())){
            throw new RuntimeException("Token expired");
        }
        return new VerificationResult(claims,token);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static class VerificationResult{
        public Claims claims;
        public String token;


        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }

}
