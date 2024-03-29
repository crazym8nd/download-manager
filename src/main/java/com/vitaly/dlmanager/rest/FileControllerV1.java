package com.vitaly.dlmanager.rest;

import com.vitaly.dlmanager.dto.SuccessResponse;
import com.vitaly.dlmanager.entity.user.UserRole;
import com.vitaly.dlmanager.mapper.FileMapper;
import com.vitaly.dlmanager.security.CustomPrincipal;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/files")
@Validated
public class FileControllerV1 {

    private final FileService fileService;
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
    @GetMapping(path = "/{id}")
    public Mono<SuccessResponse> download(@PathVariable("id") Long id,Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();
        Long userId = customPrincipal.getId();

        return fileService.getById(id)
                .flatMap(fileEntity -> {
                    if (UserRole.ADMIN.equals(role) || UserRole.MODERATOR.equals(role) || fileEntity.getUserId().equals(userId)) {
                        return fileService.downloadFile(id)
                                .map(objectKey -> new SuccessResponse(objectKey, "Object byte response"));
                    } else {
                        return Mono.just(new SuccessResponse(null, HttpStatus.FORBIDDEN.toString()));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> Mono.just(new SuccessResponse(null, HttpStatus.NOT_FOUND.toString()))));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable Long id,Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();
        Long userId = customPrincipal.getId();


        return fileService.getById(id)
                .flatMap(fileEntity -> {
                    if (UserRole.ADMIN.equals(role) || UserRole.MODERATOR.equals(role) || fileEntity.getUserId().equals(userId)) {
                        return fileService.delete(id)
                                .then(Mono.just(new ResponseEntity<>(HttpStatus.OK)));
                    } else {
                        return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
                    }
                }).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Flux<SuccessResponse> getListOfFiles(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();
        Long userId = customPrincipal.getId();

        if (UserRole.ADMIN.equals(role) || UserRole.MODERATOR.equals(role)) {
            return fileService.getAll()
                    .map(fileEntity -> new SuccessResponse(fileMapper.map(fileEntity), "Result found"));
        }
        return fileService.getAllFilesByUserId(userId)
                .map(fileEntity -> new SuccessResponse(fileMapper.map(fileEntity), "Result found"));
    }
}