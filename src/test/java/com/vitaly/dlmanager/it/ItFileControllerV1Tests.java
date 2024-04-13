package com.vitaly.dlmanager.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vitaly.dlmanager.config.MysqlTestContainerConfig;
import com.vitaly.dlmanager.config.aws.AwsProperties;
import com.vitaly.dlmanager.dto.*;
import com.vitaly.dlmanager.entity.file.FileEntity;
import com.vitaly.dlmanager.repository.FileRepository;
import com.vitaly.dlmanager.util.UserDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

@Import({MysqlTestContainerConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItFileControllerV1Tests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private S3AsyncClient s3AsyncClient;

    @Autowired
    private AwsProperties s3ConfigProperties;

    @Autowired
    public void loadContext(final ApplicationContext applicationContext) {
        webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    @Test
    void testBucket(){
        assertThat(this.s3AsyncClient.listBuckets()).isNotNull();
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
    private FileDto convertToDto(Map<String,Object> dataMap){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.convertValue(dataMap,FileDto.class);
    }


    private BodyInserters.MultipartInserter uploadFileForTesting(String fileName) {
        Resource mockFileResource = new ByteArrayResource("File content".getBytes()) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };
        return BodyInserters.fromMultipartData("form-data", mockFileResource);
    }

    //user positive
    @Test
    public void givenUserRoleUser_whenGetAllHisFiles_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBodyList(SuccessResponse.class)
                .consumeWith(response -> {
                    List<SuccessResponse> responseList = response.getResponseBody();
                    List<FileDto> fileDtoList = responseList.stream()
                            .map(successResponse -> convertToDto((Map<String, Object>) successResponse.data()))
                            .collect(Collectors.toList());
                    List<FileEntity> files = fileRepository.findAllByUserId(authResponseDto.getUserId()).collectList().block();

                    assertNotNull(files);
                    assertNotNull(fileDtoList);
                    assertEquals(files.size(), fileDtoList.size());
                    assertTrue(files.stream()
                            .map(FileEntity::getId)
                            .allMatch(fileEntityId -> fileDtoList.stream()
                                    .map(FileDto::getId)
                                    .collect(Collectors.toList())
                                    .contains(fileEntityId)));

                });
    }
    @Test
    public void givenValidFile_whenUpload_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "userTestFile.jpeg";

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Upload successfully");
    }
    @Test
    public void givenValidFile_whenDownloadFile_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "userTestFileForDL.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();
        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files/1" )
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    byte[] responseBody = response.getResponseBodyContent();
                    assertNotNull(responseBody);
                });
    }
    @Test
    public void givenUserRoleUser_whenDeleteEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "userTestFileForDelete.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().is2xxSuccessful();
    }

    //user negative
    @Test
    public void givenUserRoleUser_whenGetAllHisFiles_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files/all")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenInvalidFile_whenUpload_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "userTestFile.sql";

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenInvalidFile_whenDownloadFile_thenStatus200mesasgeBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "userTestFileForDLBR.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();
        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files/33" )
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("404 NOT_FOUND");
    }
    @Test
    public void givenUserRoleUser_whenDeleteEvent_thenStatus204mesasgeBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "userTestFileForDeleteBR.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/99")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isNotFound();
    }

    //moderator positive
    @Test
    public void givenUserRoleModerator_whenGetAllFiles_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBodyList(SuccessResponse.class)
                .consumeWith(response -> {
                    List<SuccessResponse> responseList = response.getResponseBody();
                    List<FileDto> fileDtoList = responseList.stream()
                            .map(successResponse -> convertToDto((Map<String, Object>) successResponse.data()))
                            .collect(Collectors.toList());
                    List<FileEntity> files = fileRepository.findAll().collectList().block();

                    assertNotNull(files);
                    assertNotNull(fileDtoList);
                    assertEquals(files.size(), fileDtoList.size());
                    assertTrue(files.stream()
                            .map(FileEntity::getId)
                            .allMatch(fileEntityId -> fileDtoList.stream()
                                    .map(FileDto::getId)
                                    .collect(Collectors.toList())
                                    .contains(fileEntityId)));

                });
    }
    @Test
    public void givenValidFileModerator_whenUpload_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "moderatorTestFile.jpeg";

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Upload successfully");
    }
    @Test
    public void givenValidFileModerator_whenDownloadFile_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "moderatorTestFileForDL.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();
        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files/1" )
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    byte[] responseBody = response.getResponseBodyContent();
                    assertNotNull(responseBody);
                });
    }
    @Test
    public void givenUserRoleModerator_whenDeleteEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "moderatorTestFileForDelete.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().is2xxSuccessful();
    }

    //moderator negative
    @Test
    public void givenUserRoleModerator_whenGetAllFiles_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files/all")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenInvalidFileModerator_whenUpload_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "moderatorTestFile.sql";

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenInvalidFileModerator_whenDownloadFile_thenStatus200messageBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "moderatorTestFileForDLBR.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();
        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files/33" )
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("404 NOT_FOUND");
    }
    @Test
    public void givenUserRoleModerator_whenDeleteEvent_thenStatus204messageBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "ModeratorTestFileForDeleteBR.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/99")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isNotFound();
    }

    //admin positive
    @Test
    public void givenUserRoleAdmin_whenGetAllFiles_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isOk()
                .expectBodyList(SuccessResponse.class)
                .consumeWith(response -> {
                    List<SuccessResponse> responseList = response.getResponseBody();
                    List<FileDto> fileDtoList = responseList.stream()
                            .map(successResponse -> convertToDto((Map<String, Object>) successResponse.data()))
                            .collect(Collectors.toList());
                    List<FileEntity> files = fileRepository.findAll().collectList().block();

                    assertNotNull(files);
                    assertNotNull(fileDtoList);
                    assertEquals(files.size(), fileDtoList.size());
                    assertTrue(files.stream()
                            .map(FileEntity::getId)
                            .allMatch(fileEntityId -> fileDtoList.stream()
                                    .map(FileDto::getId)
                                    .collect(Collectors.toList())
                                    .contains(fileEntityId)));

                });
    }
    @Test
    public void givenValidFileAdmin_whenUpload_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "moderatorTestFileUpload.jpeg";

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Upload successfully");
    }
    @Test
    public void givenValidFileAdmin_whenDownloadFile_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "adminTestFileForDL.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();
        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files/1" )
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    byte[] responseBody = response.getResponseBodyContent();
                    assertNotNull(responseBody);
                });
    }
    @Test
    public void givenUserRoleAdmin_whenDeleteEvent_thenSuccessResponse() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "adminTestFileForDelete.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/1")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().is2xxSuccessful();
    }

    //admin negative
    @Test
    public void givenUserRoleAdmin_whenGetAllFiles_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);

        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files/all")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();
        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenInvalidFileAdmin_whenUpload_thenReturnBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "adminTestFile.sql";

        //when
        WebTestClient.ResponseSpec result = webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //then
        result.expectStatus().isBadRequest();
    }
    @Test
    public void givenInvalidFileAdmin_whenDownloadFile_thenStatus200messageBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserModeratorDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "adminTestFileForDLBR.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();
        //when
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/v1/files/33" )
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("404 NOT_FOUND");
    }
    @Test
    public void givenUserRoleAdmin_whenDeleteEvent_thenStatus204messageBadRequest() {
        //given
        AuthRequestDto authRequestDto = UserDataUtils.getUserAdminDtoForLogin();
        AuthResponseDto authResponseDto = prepareAuthResponseForJwt(authRequestDto);
        String fileName = "adminTestFileForDeleteBR.jpeg";
        webTestClient.post().uri("/api/v1/files")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .body(uploadFileForTesting(fileName))
                .exchange();

        //when
        WebTestClient.ResponseSpec result = webTestClient.delete().uri("/api/v1/events/99")
                .header("Authorization", "Bearer " + authResponseDto.getToken())
                .exchange();

        //then
        result.expectStatus().isNotFound();
    }
}
