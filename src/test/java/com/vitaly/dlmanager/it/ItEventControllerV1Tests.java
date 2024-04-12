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
@TestExecutionListeners(
        listeners = DatabasePopulationListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
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

        EventDto newEvent = EventDataUtils.getEventDtoForSavingByUser();

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
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
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
    //user negative

        @Test
        public void givenUserRoleUser_whenGetAllHisEvents_thenBadRequest() {
            //given
            AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
            AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

            //when
            WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/events/all")
                    .header("Authorization", "Bearer " + authResponseDto.getToken())
                    .exchange();
            //then
            result.expectStatus().isBadRequest();
        }
    @Test
    public void givenUserRoleUser_whenGetEventById_thenReturnNotFound() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/events/1999")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isNotFound();
    }
    @Test
    public void givenUserRoleUser_whenCreateEvent_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/events")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("event" +"created"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserRoleUser_whenUpdateEvent_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.put().uri("/api/v1/events/4")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("update my event"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserRoleUser_whenDeleteEvent_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/843")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isNotFound();
    }

    //moderator positive
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
    @Test
    public void givenUserRoleModerator_whenGetEventById_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
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
    public void givenUserRoleModerator_whenCreateEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        EventDto newEvent = EventDataUtils.getEventDtoForSavingByModerator();

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
    public void givenUserRoleModerator_whenUpdateEvent_thenSuccessResponse() {
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
    public void givenUserRoleModerator_whenDeleteEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        EventEntity event = eventRepository.save(EventDataUtils.getEventForDeleteByModerator()).block();
        Long eventIdToDelete = event.getId();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/{id}", eventIdToDelete)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().is2xxSuccessful();
    }


    //moderator negative
    @Test
    public void givenUserRoleModerator_whenGetAllHisEvents_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/events/all")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserRoleModerator_whenGetEventById_thenReturnNotFound() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/events/1999")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isNotFound();
    }
    @Test
    public void givenUserRoleModerator_whenCreateEvent_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/events")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("event" +"created"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserRoleModerator_whenUpdateEvent_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.put().uri("/api/v1/events/4")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("update my event"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserRoleModerator_whenDeleteEvent_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/843")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isNotFound();
    }


    //admin positive
    @Test
    public void givenUserRoleAdmin_whenGetAllEvents_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
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
    @Test
    public void givenUserRoleAdmin_whenGetEventById_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
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
    public void givenUserRoleAdmin_whenCreateEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        EventDto newEvent = EventDataUtils.getEventDtoForSavingByAdmin();

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
    public void givenUserRoleAdmin_whenUpdateEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
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
    public void givenUserRoleAdmin_whenDeleteEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        EventEntity event = eventRepository.save(EventDataUtils.getEventForDeleteByAdmin()).block();
        Long eventIdToDelete = event.getId();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/{id}", eventIdToDelete)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().is2xxSuccessful();
    }
    //admin negative
    @Test
    public void givenUserRoleAdmin_whenGetAllHisEvents_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/events/all")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserRoleAdmin_whenGetEventById_thenReturnNotFound() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/events/1999")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isNotFound();
    }
    @Test
    public void givenUserRoleAdmin_whenCreateEvent_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/events")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("event" +"created"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserRoleAdmin_whenUpdateEvent_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.put().uri("/api/v1/events/4")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("update my event"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserRoleAdmin_whenDeleteEvent_thenBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/843")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isNotFound();
    }
}
