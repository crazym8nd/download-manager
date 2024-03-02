package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.file.FileEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.Optional;

public interface FileService {
    Mono<Void> upload(FileEntity fileEntity);

    InputStream download(FileEntity file);

    Flux<String> listFiles();

    void deleteFile(String fileName);
}
