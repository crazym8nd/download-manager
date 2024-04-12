package com.vitaly.dlmanager.it;

import com.vitaly.dlmanager.config.DatabasePopulationListener;
import com.vitaly.dlmanager.config.MysqlTestContainerConfig;
import com.vitaly.dlmanager.dto.AuthRequestDto;
import com.vitaly.dlmanager.dto.AuthResponseDto;
import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.repository.EventRepository;
import com.vitaly.dlmanager.repository.FileRepository;
import com.vitaly.dlmanager.repository.UserRepository;
import com.vitaly.dlmanager.util.UserDataUtils;
import org.junit.jupiter.api.Test;
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
public class ItUserControllerV1Tests {

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


    // admin positive scenarios
    @Test
    public void givenUserAdmin_whenGetAllUsers_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

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
    public void givenUserAdmin_whenGetUserById_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when

        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/users/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().is2xxSuccessful()
                .expectBody(UserDto.class)
                .consumeWith(response -> {
                    UserDto userDto = response.getResponseBody();
                    assertNotNull(userDto);
                    assertNotNull(userDto.getEvents());
                    assertEquals(1L, userDto.getId());
                });
    }
    @Test
    public void givenUserAdmin_whenCreateUser_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        UserDto newUser = UserDataUtils.getUserDtoForSavingByAdmin();

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
    public void givenUserAdmin_whenUpdateUser_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

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
    public void givenUserAdmin_whenDeleteUser_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        Long userId = userRepository.save(UserDataUtils.getUserToBeDeletedByAdminTransient()).block().getId();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/users/{id}", userId)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isOk();
    }
    //admin negative scenario
    @Test
    public void givenUserAdmin_whenGetAllUsers_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when

        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/user/all")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isNotFound();
    }

    @Test
    public void givenUserAdmin_whenGetUserById_thenReturnNoContent() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when

        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/users/999")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isNoContent();
    }
    @Test
    public void givenUserAdmin_whenCreateUser_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/users")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("newUser" + " " + "newPassword"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserAdmin_whenUpdateUser_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.put().uri("/api/v1/users/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("changeUser" + " " + "changePassword"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }

    @Test
    public void givenUserAdmin_whenDeleteUser_thenReturnNoContent() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        Long userIdToDelete = 9999L;

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/users/{id}", userIdToDelete)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().is2xxSuccessful();
    }

    // moderator positive scenario
    @Test
    public void givenUserModerator_whenGetAllUsers_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

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
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when

        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/users/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().is2xxSuccessful()
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
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        UserDto newUser = UserDataUtils.getUserDtoForSavingByMod();

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
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

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
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        Long userIdToDelete = userRepository.save(UserDataUtils.getUserToBeDeletedByModTransient()).block().getId();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/users/{id}", userIdToDelete)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isOk();
    }
    //moderator negative scenario
    @Test
    public void givenUserModerator_whenGetAllUsers_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when

        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/user/all")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isNotFound();
    }

    @Test
    public void givenUserModerator_whenGetUserById_thenReturnNoContent() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when

        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/users/999")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isNoContent();
    }
    @Test
    public void givenUserModerator_whenCreateUser_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/users")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("newUser" + " " + "newPassword"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenUserModerator_whenUpdateUser_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.put().uri("/api/v1/users/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("changeUser" + " " + "changePassword"), String.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }

    @Test
    public void givenUserModerator_whenDeleteUser_thenReturnNoContent() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        Long userIdToDelete = 9999L;

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/users/{id}", userIdToDelete)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().is2xxSuccessful();
    }


    //user negative scenario
    @Test
    public void givenUserRoleUser_whenGetAllUsers_thenUnauthorizedResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

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
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

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
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        UserDto newUser = UserDataUtils.getUserDtoUpdated();

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
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

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
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        Long userIdToDelete = 1L;

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/users/{id}", userIdToDelete)
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isForbidden();
    }
}
