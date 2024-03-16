package com.vitaly.dlmanager.rest;
//  01-Mar-24
// gh crazym8nd

import com.vitaly.dlmanager.dto.SuccessResponse;
import com.vitaly.dlmanager.mapper.FileMapper;
import com.vitaly.dlmanager.service.FileService;
import com.vitaly.dlmanager.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/files")
@Validated
public class FileControllerV1 {

    private final FileService fileService;
    private final FileMapper fileMapper;

    @PostMapping("/upload")
    public Mono<SuccessResponse> upload(@RequestPart("file-data") Mono<FilePart> filePart) {
        return filePart
                .map(file -> {
                    FileUtils.filePartValidator(file);
                    return file;
                })
                .flatMap(fileService::uploadObject)
                .map(fileResponse -> new SuccessResponse(fileResponse, "Upload successfully"));
    }

    @GetMapping(path="/{fileKey}")
    public Mono<SuccessResponse> download(@PathVariable("fileKey") String fileKey) {

        return fileService.getByteObject(fileKey)
                .map(objectKey -> new SuccessResponse(objectKey, "Object byte response"));
    }

    @DeleteMapping(path="/{objectKey}")
    public Mono<SuccessResponse> deleteFile(@PathVariable("objectKey") String objectKey) {
        return fileService.deleteObject(objectKey)
                .map(resp -> new SuccessResponse(null, MessageFormat.format("Object with key: {0} deleted successfully", objectKey)));
    }

    @GetMapping
    public Flux<SuccessResponse> getObject() {
        return fileService.getObjects()
                .map(objectKey -> new SuccessResponse(objectKey, "Result found"));
    }
}