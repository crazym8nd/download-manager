package com.vitaly.dlmanager.it;

import com.vitaly.dlmanager.config.DatabasePopulationListener;
import com.vitaly.dlmanager.config.MysqlTestContainerConfig;
import com.vitaly.dlmanager.dto.AuthRequestDto;
import com.vitaly.dlmanager.dto.AuthResponseDto;
import com.vitaly.dlmanager.dto.EventDto;
import com.vitaly.dlmanager.entity.event.EventEntity;
import com.vitaly.dlmanager.repository.EventRepository;
import com.vitaly.dlmanager.repository.FileRepository;
import com.vitaly.dlmanager.repository.UserRepository;
import com.vitaly.dlmanager.util.EventDataUtils;
import com.vitaly.dlmanager.util.FileDataUtils;
import com.vitaly.dlmanager.util.UserDataUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

@Import({MysqlTestContainerConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItEventControllerV1Tests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    public void loadContext(final ApplicationContext applicationContext) {
        webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    private AuthResponseDto prepareAuthResponseForJwt(AuthRequestDto authRequestDto) {
        return webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();
    }

        // user positive scenario
    @Test
    public void givenUserRoleUser_whenGetAllHisEvents_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/events")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBodyList(EventDto.class)
                .consumeWith(response -> {
                    List<EventDto> eventDtoList = response.getResponseBody();
                    List<EventEntity> events = eventRepository.findAllByUserId(authResponseDto.getUserId()).collectList().block();
                    assertNotNull(eventDtoList);
                    assertEquals(events.size(), eventDtoList.size());
                    assertEquals(eventDtoList.get(0).getUserId(), authResponseDto.getUserId());

                });
    }
    @Test
    public void givenUserRoleUser_whenGetEventById_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/events/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBody(EventDto.class)
                .consumeWith(response -> {
                   EventDto eventDto = response.getResponseBody();
                   EventEntity event = eventRepository.findById(1L).block();
                    assertNotNull(eventDto);
                    assertEquals(eventDto.getId(), event.getId());
                });
    }
    @Test
    public void givenUserRoleUser_whenCreateEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        EventDto newEvent = EventDataUtils.getEventDtoForSaving();

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/events")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newEvent), EventDto.class)
                .exchange();

        //then
        result.expectStatus().isCreated()
                .expectBody(EventDto.class)
                .consumeWith(response -> {
                    EventDto createdEvent = response.getResponseBody();
                    assertNotNull(createdEvent);
                    assertNotNull(createdEvent.getId());
                    assertEquals(newEvent.getUserId(), createdEvent.getUserId());
                });
    }
    @Test
    public void givenUserRoleUser_whenUpdateEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        EventDto eventForUpdate = EventDataUtils.getEventForUpdate();

        //when
        WebTestClient.ResponseSpec result = webTestClient.put().uri("/api/v1/events/4")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(eventForUpdate), EventDto.class)
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody(EventDto.class)
                .consumeWith(response -> {
                    EventDto updatedEvent = response.getResponseBody();
                    assertNotNull(updatedEvent);
                    assertNotNull(updatedEvent.getId());
                    assertEquals(eventRepository.findById(4L).block().getFileId(), updatedEvent.getFileId());
                });
    }
    @Test
    public void givenUserRoleUser_whenDeleteEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        Long eventIdToDelete = 1L;

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/{id}", eventIdToDelete)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().is2xxSuccessful();
    }

    @Test
    public void givenUserRoleModerator_whenGetAllEvents_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/events")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBodyList(EventDto.class)
                .consumeWith(response -> {
                    List<EventDto> eventDtoList = response.getResponseBody();
                    List<EventEntity> events = eventRepository.findAll().collectList().block();
                    assertNotNull(eventDtoList);
                    assertEquals(events.size(), eventDtoList.size());
                });
    }

}
