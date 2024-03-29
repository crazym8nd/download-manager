package com.vitaly.dlmanager.rest;


import com.vitaly.dlmanager.dto.EventDto;
import com.vitaly.dlmanager.entity.user.UserRole;
import com.vitaly.dlmanager.mapper.EventMapper;
import com.vitaly.dlmanager.security.CustomPrincipal;
import com.vitaly.dlmanager.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/events")
@RequiredArgsConstructor
public class EventControllerV1 {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EventDto>> getEventById(Authentication authentication, @PathVariable Long id) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();
        Long userId = customPrincipal.getId();

        return eventService.getById(id)
                .flatMap(event -> {
                    if (UserRole.ADMIN.equals(role) || UserRole.MODERATOR.equals(role) || event.getUserId().equals(userId)) {
                        return Mono.just(ResponseEntity.ok(eventMapper.map(event)));
                    } else {
                        return Mono.just(new ResponseEntity<EventDto>(HttpStatus.FORBIDDEN));
                    }
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<EventDto>>> getAllEvents(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();
        Long userId = customPrincipal.getId();

        Flux<EventDto> eventDtoFlux;
        if (UserRole.USER.equals(role)) {
            eventDtoFlux = eventService.getAllByUserId(userId)
                    .map(eventMapper::map);
        } else if (UserRole.MODERATOR.equals(role) || UserRole.ADMIN.equals(role)) {
            eventDtoFlux = eventService.getAll()
                    .map(eventMapper::map);
        } else {
            eventDtoFlux = Flux.empty();
        }
        return Mono.just(ResponseEntity.ok(eventDtoFlux));
    }


    @PostMapping
    public Mono<ResponseEntity<EventDto>> save(@RequestBody EventDto eventDto) {
        if (eventDto == null) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }


        return eventService.save(eventMapper.map(eventDto))
                .map(savedEvent -> new ResponseEntity<>(eventMapper.map(savedEvent), HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<EventDto>> update(@PathVariable Long id, @RequestBody EventDto eventDto, Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();
        Long userId = customPrincipal.getId();

        return eventService.getById(id)
                .flatMap(event -> {
                    if (UserRole.ADMIN.equals(role) || UserRole.MODERATOR.equals(role) || event.getUserId().equals(userId)) {
                        return Mono.just(ResponseEntity.ok(eventMapper.map(event)));
                    } else {
                        return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
                    }
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable Long id, Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();
        Long userId = customPrincipal.getId();


        return eventService.getById(id)
                .flatMap(event -> {
                    if (UserRole.ADMIN.equals(role) || UserRole.MODERATOR.equals(role) || event.getUserId().equals(userId)) {
                        return eventService.delete(id)
                                .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
                    } else {
                        return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
                    }
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
