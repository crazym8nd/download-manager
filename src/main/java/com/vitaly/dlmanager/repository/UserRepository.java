package com.vitaly.dlmanager.repository;
//  17-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.user.UserEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<UserEntity, Long> {
    @Modifying
    @Query("UPDATE users SET status = 'DELETED' WHERE id = :id")
    Mono<Void> deleteById(Long id);

    Mono<UserEntity> findByUsername(String username);
}
