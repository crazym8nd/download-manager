package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.dto.FileResponse;
import com.vitaly.dlmanager.entity.file.FileEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface FileService {
    Mono<FileResponse> uploadObject(FilePart filePart, Long userId);

    Mono<byte[]> downloadFile(Long fileId);

   Flux<FileEntity> getAllFilesByUserId(Optional<Long> userId);

    Mono<FileEntity> delete(Long id);

    Mono<FileEntity> getById(Long id);
}
