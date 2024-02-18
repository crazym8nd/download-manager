package com.vitaly.dlmanager.repository;
//  17-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<UserEntity,Long> {

    Mono<UserEntity> findByUsername(String username);
}
