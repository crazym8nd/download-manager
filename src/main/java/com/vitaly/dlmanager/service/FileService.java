package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.file.FileEntity;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface FileService {
    String uploadFile(MultipartFile file);

    byte[] downloadFile(String fileName);

    Flux<String> listFiles();

    void delete(String fileName);
}
