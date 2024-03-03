package com.vitaly.dlmanager.rest;
//  01-Mar-24
// gh crazym8nd

import com.vitaly.dlmanager.dto.FileDto;
import com.vitaly.dlmanager.mapper.FileMapper;
import com.vitaly.dlmanager.service.FileService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            fileService.deleteFile(fileName);
            return new ResponseEntity<>(fileName + " deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public Mono<ResponseEntity<String>> upload(
            @RequestBody @NonNull FileDto fileDto) {
        return fileService.upload(fileMapper.map(fileDto))
                .map(response -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("File Uploaded Successfully"));
    }

    @PutMapping
    public ResponseEntity<InputStreamResource> download(@RequestBody @NonNull FileDto fileDto){
        if (fileDto.getFileName() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File name is required");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileDto.getFileName());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStreamResource resource = new InputStreamResource(fileService.download(fileMapper.map(fileDto)));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
