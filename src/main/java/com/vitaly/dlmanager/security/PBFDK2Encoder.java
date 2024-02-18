package com.vitaly.dlmanager.security;
//  17-Feb-24
// gh crazym8nd


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PBFDK2Encoder implements PasswordEncoder {

@Value("${jwt.password.encoder.secret}")
    private String secret;

    @Value("${jwt.password.encoder.iteration}")
    private Integer iteration;

    @Value("${jwt.password.encoder.keyLength}")
    private Integer keyLength;

    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA512";

    // getting encoded password
    @Override
    public String encode(CharSequence password) {

        if(password == null || password.length() == 0) {
            throw new IllegalArgumentException("password cannot be null or empty");
        }

        try {
            byte [] result = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE)
                    .generateSecret(new PBEKeySpec(password.toString().toCharArray(),
                            secret.getBytes(), iteration, keyLength))
                    .getEncoded();

            return Base64.getEncoder()
                    .encodeToString(result);
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    // token validation
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);

    }
}