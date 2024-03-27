package com.vitaly.dlmanager.rest;
//  01-Mar-24
// gh crazym8nd

import com.vitaly.dlmanager.dto.SuccessResponse;
import com.vitaly.dlmanager.entity.user.UserRole;
import com.vitaly.dlmanager.mapper.FileMapper;
import com.vitaly.dlmanager.security.CustomPrincipal;
import com.vitaly.dlmanager.security.SecurityService;
import com.vitaly.dlmanager.service.FileService;
import com.vitaly.dlmanager.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/files")
@Validated
public class FileControllerV1 {

    private final FileService fileService;
    private final SecurityService securityService;
    private final FileMapper fileMapper;

    @PostMapping()
    public Mono<SuccessResponse> upload(@RequestPart("form-data") Mono<FilePart> filePart
            , Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        Long userId = customPrincipal.getId();
        return filePart
                .flatMap(file -> {
                    FileUtils.filePartValidator(file);
                    return fileService.uploadObject(file, userId);
                })
                .map(fileResponse -> new SuccessResponse(fileResponse, "Upload successfully"));
    }

    @GetMapping(path = "/{fileKey}")
    public Mono<SuccessResponse> download(@PathVariable("fileKey") String fileKey) {

        return fileService.getByteObject(fileKey)
                .map(objectKey -> new SuccessResponse(objectKey, "Object byte response"));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
        return fileService.getById(id)
                .flatMap(event -> fileService.delete(event.getId()))
                .then(Mono.just(new ResponseEntity<>(HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Flux<SuccessResponse> getObject(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();
        Optional<Long> userIdOptional = UserRole.USER.equals(role) ? Optional.of(customPrincipal.getId()) : Optional.empty();

        return fileService.getAllFilesByUserId(userIdOptional)
                .map(fileEntity -> new SuccessResponse(fileMapper.map(fileEntity), "Result found"));
    }
}