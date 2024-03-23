package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.dto.AWSS3Object;
import com.vitaly.dlmanager.dto.FileResponse;
import com.vitaly.dlmanager.entity.file.FileEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService extends GenericService<FileEntity, Long> {
    Mono<FileResponse> uploadObject(FilePart filePart);

    Mono<byte[]> getByteObject(@NotNull String key);

    Flux<AWSS3Object> getObjects();

    Mono<Void> deleteObject(@NotNull String objectKey);

    Mono<FileEntity> getById(Long id);
}
