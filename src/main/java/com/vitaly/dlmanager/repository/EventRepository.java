package com.vitaly.dlmanager.repository;
    


import com.vitaly.dlmanager.entity.event.EventEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepository extends R2dbcRepository<EventEntity, Long> {
    @Modifying
    @Query("UPDATE events SET status = 'DELETED' WHERE id = :id")
    Mono<Void> deleteById(Long id);

    Flux<EventEntity> findAllByUserId(Long userId);
}
