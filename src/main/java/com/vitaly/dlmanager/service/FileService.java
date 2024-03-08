package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<String> uploadFile(MultipartFile file);

    Mono<byte[]> downloadFile(String fileName);

    Flux<String> listFiles();

    Mono<Void> delete(String fileName);
}
