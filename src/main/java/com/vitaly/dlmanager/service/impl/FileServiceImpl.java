package com.vitaly.dlmanager.service.impl;
//  22-Feb-24
// gh crazym8nd


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.vitaly.dlmanager.entity.file.FileEntity;
import com.vitaly.dlmanager.repository.FileRepository;
import com.vitaly.dlmanager.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private FileRepository fileRepository;

    private  AmazonS3 s3client;

    @Value("${s3.bucketName}")
    private String BUCKET_NAME;

    public FileServiceImpl(AmazonS3 s3client){
        this.s3client =s3client;
    }
    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    public void createBucket(){
        String bucketName = BUCKET_NAME;

        if(s3client.doesBucketExistV2(bucketName)){
            log.info("Bucket {} already exists", bucketName);
            return;
        }
        s3client.createBucket(bucketName);
    }

    public void listOfBuckets(){
        List<Bucket> buckets = s3client.listBuckets();
        log.info("list of buckets: {}", buckets);
    }

    @Override
    public Mono<Void> upload(FileEntity file) {
        java.io.File fileToUpload = new java.io.File(file.getLocation());
        file.setCreatedAt(LocalDateTime.now());
        log.info("File uploaded {}", file.getFileName());

        try {
            s3client.putObject(BUCKET_NAME, file.getFileName(), fileToUpload);
        } catch(AmazonServiceException e){
            throw new ResponseStatusException(HttpStatusCode.valueOf(e.getStatusCode()), "Error uploading file", e);
        } catch (SdkClientException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error #2 uploading file", e);
        }
        return Mono.empty();
    }

    @Override
    public InputStream download(FileEntity file) {
        String bucketName = BUCKET_NAME;
        if (s3client.doesObjectExist(bucketName, file.getFileName())){
            S3Object s3Object = s3client.getObject(bucketName, file.getFileName());
            log.info("File download {}", file.getFileName());
            return s3Object.getObjectContent();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not exist?");
        }
    }

    @Override
    public Flux<String> listFiles() {
        String bucketName = BUCKET_NAME;
        ObjectListing objectListing = s3client.listObjects(bucketName);
        if(objectListing != null){
            log.info("All files");
            return Flux.fromIterable(objectListing.getObjectSummaries())
                    .map(S3ObjectSummary::getKey);
        } else {
            log.info("No files");
            return Flux.empty();
        }
    }

    @Override
    public void deleteFile(String fileName) {
        String bucketName = BUCKET_NAME;
        s3client.deleteObject(bucketName, fileName);
        log.info("File deleted {}", fileName);
    }
}
