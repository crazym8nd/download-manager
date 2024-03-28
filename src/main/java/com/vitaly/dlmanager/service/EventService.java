package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.event.EventEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface EventService extends GenericService<EventEntity, Long> {

    Flux<EventEntity> getAllByUserId(Long userId);

    Mono<Boolean> userHasEvent(Long userId, Long eventId);

}
