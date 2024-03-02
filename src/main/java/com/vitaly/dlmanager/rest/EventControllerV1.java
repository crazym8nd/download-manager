package com.vitaly.dlmanager.rest;
//  01-Mar-24
// gh crazym8nd

import com.vitaly.dlmanager.dto.EventDto;
import com.vitaly.dlmanager.entity.event.EventEntity;
import com.vitaly.dlmanager.mapper.EventMapper;
import com.vitaly.dlmanager.repository.EventRepository;
import com.vitaly.dlmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/events/")
public class EventControllerV1 {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @Autowired
    public EventControllerV1(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EventDto>> getEventById(@PathVariable Long id){
        return eventService.getById(id)
                .map(event ->{
                    EventDto eventDto = eventMapper.map(event);
                    return ResponseEntity.ok(eventDto);
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping
    public Flux<ResponseEntity<EventDto>> getAllEvents(){
        return eventService.getAll()
                .map(eventMapper::map)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Flux.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));

    }

    @PostMapping
    public Mono<ResponseEntity<EventEntity>> save(@RequestBody EventEntity eventEntity){
        if (eventEntity == null){
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return eventService.save(eventEntity)
                .map(savedEvent -> new ResponseEntity<>(savedEvent, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<EventEntity>> update(@PathVariable Long id, @RequestBody EventEntity eventEntity){
        if (eventEntity == null){
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return eventService.update(eventEntity)
                .map(updatedEvent -> new ResponseEntity<>(updatedEvent, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id){
        return eventService.getById(id)
                .flatMap(event -> eventService.delete(event.getId()))
                .then(Mono.just(new ResponseEntity<>(HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
