package com.vitaly.dlmanager.service.impl;
//  17-Feb-24
// gh crazym8nd

import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.entity.user.UserRole;
import com.vitaly.dlmanager.repository.UserRepository;
import com.vitaly.dlmanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserEntity> registerUser(UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> log.info("IN registerUserUser - user {} created", u));
    }


    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Flux<UserEntity> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public Mono<UserEntity> getById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional
    public Mono<UserEntity> update(UserEntity userEntity) {
        return this.userRepository.findById(userEntity.getId())
                .flatMap((u -> {
                    u.setUpdatedAt(LocalDateTime.now());
                    return this.userRepository.save(userEntity);
                }));
    }

    @Override
    public Mono<UserEntity> save(UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> log.info("IN registerUserUser - user {} created", u));
    }

    @Override
    @Transactional
    public Mono<UserEntity> delete(Long id) {
        return this.userRepository
                .findById(id)
                .flatMap(u ->
                        this.userRepository.deleteById(u.getId()).thenReturn(u));
    }
}
