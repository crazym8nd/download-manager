package com.vitaly.dlmanager.service.impl;
//  17-Feb-24
// gh crazym8nd

import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.entity.user.UserRole;
import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.repository.UserRepository;
import com.vitaly.dlmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> registerUser(UserEntity user){
        return userRepository.save(
                user.toBuilder()
                .password(passwordEncoder.encode(user.getPassword()))
                .role(UserRole.USER)
                .status(Status.ACTIVE)
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

    @Override
    public UserEntity getById(Long aLong) {
        return null;
    }

    @Override
    public List<UserEntity> getAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }
}
