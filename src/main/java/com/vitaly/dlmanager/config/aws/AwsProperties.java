package com.vitaly.dlmanager.config.aws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    private String accessKey;


    private String secretKey;


    private String region;


    private String s3BucketName;


    private int multipartMinPartSize;


    private String endpoint;
}
