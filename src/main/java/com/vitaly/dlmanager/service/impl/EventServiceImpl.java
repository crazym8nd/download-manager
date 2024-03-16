package com.vitaly.dlmanager.service.impl;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.event.EventEntity;
import com.vitaly.dlmanager.repository.EventRepository;
import com.vitaly.dlmanager.service.EventService;
import com.vitaly.dlmanager.service.FileService;
import com.vitaly.dlmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final FileService fileService;

    @Override
    public Flux<EventEntity> getAll() {
        return this.eventRepository.findAll();
    }

    @Override
    public Mono<EventEntity> getById(Long id) {
        //TODO получаем только ид без самого юзера и файла
        return this.eventRepository.findById(id)
                .flatMap(eventEntity -> Mono.zip(userService.getById(eventEntity.getUserId()),
                            fileService.getById(eventEntity.getFileId()))
                            .map(tuples -> {
                                eventEntity.setUser(tuples.getT1());
                                eventEntity.setFile(tuples.getT2());

                                return eventEntity;
                            }));
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
        ).doOnSuccess(e -> log.info("IN createEvent - event {} crated", e));
    }

    @Override
    public Mono<EventEntity> delete(Long id) {
        return this.eventRepository
                .findById(id)
                .flatMap(e ->
                        this.eventRepository.deleteById(e.getId()).thenReturn(e));
    }
}
