package com.vitaly.dlmanager.repository;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.file.FileEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileRepository extends R2dbcRepository<FileEntity, Long> {
    @Modifying
    @Query("UPDATE files SET status = 'DELETED' WHERE id = :id")
    Mono<Void> deleteById(Long id);

    Flux<FileEntity> findAllByUserId(Long userId);
}
