package com.vitaly.dlmanager.service;
    


import com.vitaly.dlmanager.entity.user.UserEntity;
import reactor.core.publisher.Mono;

public interface UserService extends GenericService<UserEntity, Long> {
    Mono<UserEntity> getUserByUsername(String username);
    Mono<UserEntity> registerUser(UserEntity user);

}
