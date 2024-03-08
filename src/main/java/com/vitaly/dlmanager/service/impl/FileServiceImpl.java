package com.vitaly.dlmanager.service.impl;
//  22-Feb-24
// gh crazym8nd


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.vitaly.dlmanager.repository.FileRepository;
import com.vitaly.dlmanager.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    private final AmazonS3 s3client;

    @Value("${s3.bucketName}")
    private String BUCKET_NAME;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, AmazonS3 s3client) {
        this.fileRepository = fileRepository;
        this.s3client = s3client;
    }


    public Mono<Void> createBucket() {
        return Mono.fromRunnable(() -> {
            String bucketName = BUCKET_NAME;
            if (!s3client.doesBucketExistV2(bucketName)) {
                s3client.createBucket(bucketName);
            } else {
                log.info("Bucket {} already exists", bucketName);
            }
        });
    }

    public Flux<Bucket> listOfBuckets() {
        return Flux.fromIterable(s3client.listBuckets())
                .doOnNext(bucket -> log.info("Bucket: {}", bucket.getName()));
    }

    @Override
    public Mono<String> uploadFile(MultipartFile file) {
        return Mono.fromCallable(() -> {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            s3client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, convertMultipartFileToFile(file)));
            log.info("File uploaded {}", fileName);
            return "File uploaded " + fileName;
        });
    }

    @Override
    public Mono<byte[]> downloadFile(String fileName) {
        return Mono.fromCallable(() -> {
            S3Object s3Object = s3client.getObject(BUCKET_NAME, fileName);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            try {
                return IOUtils.toByteArray(s3ObjectInputStream);
            } catch (IOException e) {
                log.error("Error downloading file {}", fileName, e);
                throw new RuntimeException(e);
            }
        });
    }


    @Override
    public Flux<String> listFiles() {
        return Mono.fromCallable(() -> s3client.listObjects(BUCKET_NAME))
                .flatMapMany(objectListing -> Flux.fromIterable(objectListing.getObjectSummaries()))
                .map(S3ObjectSummary::getKey)
                .doOnNext(fileName -> log.info("File: {}", fileName));
    }


    @Override
    public Mono<Void> delete(String fileName) {
        return Mono.fromRunnable(() -> {
            s3client.deleteObject(BUCKET_NAME, fileName);
            log.info("File deleted {}", fileName);
        });
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
