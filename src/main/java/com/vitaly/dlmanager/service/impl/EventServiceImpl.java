package com.vitaly.dlmanager.service.impl;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.event.EventEntity;
import com.vitaly.dlmanager.repository.EventRepository;
import com.vitaly.dlmanager.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Flux<EventEntity> getAll() {
        return this.eventRepository.findAll();
    }

    @Override
    public Mono<EventEntity> getById(Long id) {
        return this.eventRepository.findById(id);
    }

    @Override
    @Transactional
    public Mono<EventEntity> update(EventEntity eventEntity) {
        return this.eventRepository.findById(eventEntity.getId())
                .flatMap((e -> {
                    e.setStatus(eventEntity.getStatus());
                    return this.eventRepository.save(eventEntity);
                }));
    }

    @Override
    public Mono<EventEntity> save(EventEntity eventEntity) {
        return this.eventRepository.save(
                eventEntity.toBuilder()
                        .file(null)
                        .user(null)
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(e -> {
            log.info("IN createEvent - event {} crated", e);
        });
    }

    @Override
    public Mono<EventEntity> delete(Long id) {
        return this.eventRepository
                .findById(id)
                .flatMap(e ->
                        this.eventRepository.deleteById(e.getId()).thenReturn(e));
    }
}
