package com.vitaly.dlmanager.it;

import com.vitaly.dlmanager.config.JwtTokenGenerator;
import com.vitaly.dlmanager.config.MysqlTestContainerConfig;
import com.vitaly.dlmanager.service.UserService;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(MysqlTestContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
public class ItUserControllerV1Tests {

    @Autowired
    private UserService userService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

//    @Test
//    public void registerUser(){
//        UserDto userDto = UserDataUtils.getUserDtoForRegistration();
//
//        webTestClient.post()
//                .uri("api/v1/auth/register")
//                .bodyValue(userDto)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody(UserDto.class).consumeWith(response -> {
//                    UserDto responseBody = response.getResponseBody();
//                    assertNotNull(responseBody);
//                });
//    }
}
