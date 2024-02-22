package com.vitaly.dlmanager.service.impl;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.file.FileEntity;
import com.vitaly.dlmanager.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    // amazon s3?
    @Override
    public void upload(FileEntity file) {

    }

    @Override
    public InputStream download(FileEntity file) {
        return null;
    }

    @Override
    public Optional<String> listFiles() {
        return Optional.empty();
    }

    @Override
    public void deleteFile(String fileName) {

    }
}
