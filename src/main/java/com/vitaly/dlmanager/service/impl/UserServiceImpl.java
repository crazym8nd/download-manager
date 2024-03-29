package com.vitaly.dlmanager.service.impl;


import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.entity.user.UserRole;
import com.vitaly.dlmanager.repository.EventRepository;
import com.vitaly.dlmanager.repository.UserRepository;
import com.vitaly.dlmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventRepository eventRepository;


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
    public Mono<UserEntity> getUserWIthEvents(Long id){
        return userRepository.findById(id)
                .flatMap(userEntity -> eventRepository.findAllByUserId(id)
                        .collectList()
                        .doOnNext(userEntity::setEvents)
                        .thenReturn(userEntity));
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
