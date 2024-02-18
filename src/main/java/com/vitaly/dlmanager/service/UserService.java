package com.vitaly.dlmanager.service;
//  17-Feb-24
// gh crazym8nd

import com.vitaly.dlmanager.entity.UserEntity;
import com.vitaly.dlmanager.entity.UserRole;
import com.vitaly.dlmanager.entity.UserStatus;
import com.vitaly.dlmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> registerUser(UserEntity user){
        return userRepository.save(
                user.toBuilder()
                .password(passwordEncoder.encode(user.getPassword()))
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
                ).doOnSuccess(u -> {
                    log.info("IN registerUserUser - user {} created", u);
        });
    }
    public Mono<UserEntity> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Mono<UserEntity> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
