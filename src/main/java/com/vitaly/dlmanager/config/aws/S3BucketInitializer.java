package com.vitaly.dlmanager.config.aws;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@RequiredArgsConstructor
public class S3BucketInitializer {

    private final S3AsyncClient s3AsyncClient;
    private final AwsProperties s3ConfigProperties;

    @PostConstruct
    public void createBucketIfNotExists() {
        s3AsyncClient.listBuckets()
                .thenAccept(listBucketsResponse -> {
                    boolean bucketExists = listBucketsResponse.buckets().stream()
                            .anyMatch(bucket -> bucket.name().equals(s3ConfigProperties.getS3BucketName()));
                    if (!bucketExists) {
                        createBucket(s3ConfigProperties.getS3BucketName());
                    }
                })
                .exceptionally(exception -> {
                    if (exception.getCause() instanceof S3Exception s3Exception) {
                        if (s3Exception.statusCode() == 404) {
                            createBucket(s3ConfigProperties.getS3BucketName());
                            return null;
                        }
                    }
                    throw new RuntimeException("Failed to list S3 buckets", exception);
                })
                .join();
    }

    private void createBucket(String bucketName) {
        s3AsyncClient.createBucket(CreateBucketRequest.builder().bucket(bucketName).build())
                .thenAccept(createBucketResponse -> System.out.println("Bucket created: " + bucketName))
                .exceptionally(throwable -> {
                    System.err.println("Failed to create bucket: " + throwable.getMessage());
                    return null;
                });
    }
}