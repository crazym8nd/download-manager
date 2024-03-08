package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.user.UserEntity;
import reactor.core.publisher.Mono;

public interface UserService extends GenericService<UserEntity, Long> {
    Mono<UserEntity> getUserByUsername(String username);

}
