package com.vitaly.dlmanager.it;

import com.vitaly.dlmanager.config.MysqlTestContainerConfig;
import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.repository.UserRepository;
import com.vitaly.dlmanager.service.UserService;
import com.vitaly.dlmanager.util.UserDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(MysqlTestContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ItUserControllerV1Tests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll().block();
    }

    @Test
    public void givenUserDto_whenCreateUser_thenSuccessResponse(){
        //given
        UserDto userDto = UserDataUtils.getFirstUserDtoTransient();
        //when
        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userDto), UserDto.class)
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.username").isEqualTo("testname")
                .jsonPath("$.status").isEqualTo("ACTIVE");
        ;
    }
}
