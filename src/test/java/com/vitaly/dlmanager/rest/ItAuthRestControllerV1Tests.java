package com.vitaly.dlmanager.rest;

import com.vitaly.dlmanager.config.MysqlTestContainerConfig;
import com.vitaly.dlmanager.dto.AuthRequestDto;
import com.vitaly.dlmanager.dto.AuthResponseDto;
import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.user.UserEntity;
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
    private PasswordEncoder passwordEncoder;

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
        UserEntity user = UserDataUtils.getFirstUserTransient();
        user.setPassword(passwordEncoder.encode((user.getPassword())));
        userRepository.save(user).block();
    }

    @Test
    public void givenUserDto_whenLoginUser_thenSuccessResponse() {

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
        result.expectStatus().isOk();
    }
    @Test
    public void givenUserDto_whenUserInfo_thenSuccessResponse() {

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
                    assertEquals(UserDataUtils.getFirstUserDtoPersisted().getUsername(), userDtoReturned.getUsername());
                    assertEquals(UserDataUtils.getFirstUserDtoPersisted().getPassword(), userDtoReturned.getPassword());
                });
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteByUsername("testuser").block();
    }
}
