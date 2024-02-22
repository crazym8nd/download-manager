package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.file.FileEntity;

import java.io.InputStream;
import java.util.Optional;

public interface FileService {
    void upload(FileEntity file);

    InputStream download(FileEntity file);

    Optional<String> listFiles();

    void deleteFile(String fileName);
}
