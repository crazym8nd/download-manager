package com.vitaly.dlmanager.it;

import com.vitaly.dlmanager.config.MysqlTestContainerConfig;
import com.vitaly.dlmanager.dto.AuthRequestDto;
import com.vitaly.dlmanager.dto.AuthResponseDto;
import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.entity.user.UserRole;
import com.vitaly.dlmanager.repository.UserRepository;
import com.vitaly.dlmanager.util.UserDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;


@Import({MysqlTestContainerConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItAuthRestControllerV1Tests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public void loadContext(final ApplicationContext applicationContext) {
        webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    @BeforeEach
    public void setUp() {
        userRepository.save(UserDataUtils.getFirstUserTransient()).block();
    }
    @AfterEach
    public void tearDown() {
        userRepository.deleteByUsername("testuser").block();
    }

    @Test
    public void givenAuthRequestDto_whenLoginUser_thenSuccessResponse() {

        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequestDto)
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isNotEmpty();
    }

    @Test
    public void givenUserDto_whenRegisterUser_thenSuccessResponse() {

        //given
        UserDto userDto  = UserDataUtils.getUserDtoForRegister();
        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .exchange();
        //then
        result.expectStatus().isCreated();
    }
    @Test
    public void givenAuthRequestDto_whenUserInfo_thenSuccessResponse() {

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
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/auth/info")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBody(UserDto.class)
                .consumeWith(response -> {
                    UserDto userDtoReturned = response.getResponseBody();
                    assertEquals(UserDataUtils.getFirstUserTransient().getUsername(), userDtoReturned.getUsername());
                    assertEquals(UserDataUtils.getFirstUserTransient().getPassword(), userDtoReturned.getPassword());
                });
    }
    @Test
    public void givenAuthRequestDto_whenLoginUser_thenReturnsUnauthorized()  {

        //given
        AuthRequestDto authRequestDto = AuthRequestDto.builder()
                .username("blabla")
                .password("blabla").build();
        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequestDto)
                .exchange();
        //then
        result.expectStatus().isUnauthorized();
    }
    @Test
    public void givenWrongRequest_whenRegisterUser_thenReturnsBadRequest() {

        //given
        String wrongRequest = "user" + " " + "password";
        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(wrongRequest)
                .exchange();
        //then
        result.expectStatus().isBadRequest();
    }

    @Test
    public void givenWrongRequest_whenUserInfo_thenReturnBadRequest() {

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
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/auth/info")
                .exchange();
        //then
        result.expectStatus().isUnauthorized();
    }
}
