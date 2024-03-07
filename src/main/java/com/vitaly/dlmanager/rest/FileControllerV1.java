package com.vitaly.dlmanager.rest;
//  01-Mar-24
// gh crazym8nd

import com.vitaly.dlmanager.dto.FileDto;
import com.vitaly.dlmanager.mapper.FileMapper;
import com.vitaly.dlmanager.service.FileService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/files/")
public class FileControllerV1 {

    private final FileService fileService;

    private final FileMapper fileMapper;

    @Autowired
    public FileControllerV1(FileService fileService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @GetMapping
    public Mono<ResponseEntity<List<String>>> listOfFiles() {
        return fileService.listFiles()
                .collectList()
                .map(fileNames -> ResponseEntity.ok().body(fileNames))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> delete(@PathVariable String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            fileService.delete(fileName);
            return new ResponseEntity<>(fileName + " deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestPart("file") @NonNull MultipartFile file) {
        return new ResponseEntity<>(fileService.uploadFile(file), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] content = fileService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(content.length)
                .body(resource);
    }

}
