package com.vitaly.dlmanager.it;

import com.vitaly.dlmanager.config.MysqlTestContainerConfig;
import com.vitaly.dlmanager.dto.AuthRequestDto;
import com.vitaly.dlmanager.dto.AuthResponseDto;
import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.repository.UserRepository;
import com.vitaly.dlmanager.util.UserDataUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

@Import({MysqlTestContainerConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItUserControllerV1Tests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public void loadContext(final ApplicationContext applicationContext) {
        webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    //given
    //when
    //then
    @BeforeAll
    public void setUp() {
        List<UserEntity> users = List.of(UserDataUtils.getFirstUserTransient(),UserDataUtils.getSecondUserTransient(),UserDataUtils.getThirdUserTransient());
        userRepository.saveAll(users).blockLast();
    }


    // moderator positive scenario
    @Test
    public void givenUserModerator_whenGetAllUsers_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        //when

        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/users")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .consumeWith(response -> {
                    List<UserDto> userDtoList = response.getResponseBody();
                    List<UserEntity> users = userRepository.findAll().collectList().block();
                    assertNotNull(userDtoList);
                    assertEquals(users.size(), userDtoList.size());
                });
    }

    @Test
    public void givenUserModerator_whenGetUserById_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        //when

        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/users/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBody(UserDto.class)
                .consumeWith(response -> {
                    UserDto userDto = response.getResponseBody();
                    assertNotNull(userDto);
                    assertNotNull(userDto.getEvents());
                    assertEquals(1L, userDto.getId());
                });
    }
    @Test
    public void givenUserModerator_whenCreateUser_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        UserDto newUser = UserDataUtils.getUserDtoForSaving();

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/users")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newUser), UserDto.class)
                .exchange();

        //then
        result.expectStatus().isCreated()
                .expectBody(UserDto.class)
                .consumeWith(response -> {
                    UserDto createdUser = response.getResponseBody();
                    assertNotNull(createdUser);
                    assertNotNull(createdUser.getId());
                    assertEquals(newUser.getUsername(), createdUser.getUsername());
                });
    }
    @Test
    public void givenUserModerator_whenUpdateUser_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        UserDto updateUser = UserDataUtils.getUserDtoUpdated();

        //when
        WebTestClient.ResponseSpec result = webTestClient.put().uri("/api/v1/users/{id}", updateUser.getId())
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUser), UserDto.class)
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody(UserDto.class)
                .consumeWith(response -> {
                    UserDto updatedUser = response.getResponseBody();
                    assertNotNull(updatedUser);
                    assertEquals(updateUser.getUsername(), updatedUser.getUsername());
                });
    }

    @Test
    public void givenUserModerator_whenDeleteUser_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        Long userIdToDelete = 3L;

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/users/{id}", userIdToDelete)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isOk();
    }


    //user negative scenario
    @Test
    public void givenUserRoleUser_whenGetAllUsers_thenUnauthorizedResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/users")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isForbidden();
    }

    @Test
    public void givenUserRoleUser_whenGetUserById_thenUnauthorizedResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/users/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isForbidden();
    }

    @Test
    public void givenUserRoleUser_whenCreateUser_thenUnauthorizedResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        UserDto newUser = UserDataUtils.getUserDtoForSaving();

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/users")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newUser), UserDto.class)
                .exchange();

        //then
        result.expectStatus().isForbidden();
    }

    @Test
    public void givenUserRoleUser_whenUpdateUser_thenUnauthorizedResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin(); // Assuming this method returns a regular user's login details
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        UserDto updateUser = UserDataUtils.getUserDtoUpdated();

        //when
        WebTestClient.ResponseSpec result = webTestClient.put().uri("/api/v1/users/{id}", updateUser.getId())
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUser), UserDto.class)
                .exchange();

        //then
        result.expectStatus().isForbidden();
    }

    @Test
    public void givenUserRoleUser_whenDeleteUser_thenUnauthorizedResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequestDto), AuthRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponseDto.class)
                .returnResult()
                .getResponseBody();

        Long userIdToDelete = 2L;

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/users/{id}", userIdToDelete)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isForbidden();
    }
}
