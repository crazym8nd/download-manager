package com.vitaly.dlmanager.service.impl;
//  22-Feb-24
// gh crazym8nd


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.vitaly.dlmanager.entity.file.FileEntity;
import com.vitaly.dlmanager.repository.FileRepository;
import com.vitaly.dlmanager.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private FileRepository fileRepository;

    private  AmazonS3 s3client;

    @Value("${s3.bucketName}")
    private String BUCKET_NAME;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository,AmazonS3 s3client) {
        this.fileRepository = fileRepository;
        this.s3client =s3client;
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
    public String uploadFile(MultipartFile file) {
        String fileName = System.currentTimeMillis()+ "_" + file.getOriginalFilename();
        s3client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, convertMultipartFileToFile(file)));
        log.info("File uploaded {}", fileName);
        return "File uploaded " + fileName;
    }

    @Override
    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3client.getObject(BUCKET_NAME, fileName);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        try{
            byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
            return content;
        } catch (IOException e){
            log.error("Error downloading file {}", fileName, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
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
    public void delete(String fileName) {
        String bucketName = BUCKET_NAME;
        s3client.deleteObject(bucketName, fileName);
        log.info("File deleted {}", fileName);
    }

    private File convertMultipartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
