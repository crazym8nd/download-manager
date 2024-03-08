package com.vitaly.dlmanager.rest;
//  01-Mar-24
// gh crazym8nd

import com.vitaly.dlmanager.mapper.FileMapper;
import com.vitaly.dlmanager.service.FileService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/files")
public class FileControllerV1 {

    private final FileService fileService;

    private final FileMapper fileMapper;

    @Autowired
    public FileControllerV1(FileService fileService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<String>>> listOfFiles() {
        Flux<String> fileNames = fileService.listFiles();
        return Mono.just(ResponseEntity.ok().body(fileNames));
    }

    @DeleteMapping("/{fileName}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return fileService.delete(fileName)
                .thenReturn(new ResponseEntity<>(fileName + " deleted successfully.", HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @PostMapping
    public Mono<ResponseEntity<String>> uploadFile(@RequestPart("file") @NonNull MultipartFile file) {
        return fileService.uploadFile(file)
                .map(uploadedFileName -> new ResponseEntity<>(uploadedFileName, HttpStatus.OK));
    }

    @PutMapping("/{fileName}")
    public Mono<ResponseEntity<ByteArrayResource>> downloadFile(@PathVariable String fileName) {
        return Mono.fromCallable(() -> fileService.downloadFile(fileName))
                .flatMap(content -> {
                    ByteArrayResource resource = new ByteArrayResource(content.block());
                    return Mono.just(ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                            .contentLength(resource.contentLength())
                            .body(resource));
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}