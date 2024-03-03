package com.vitaly.dlmanager.config;
//  01-Mar-24
// gh crazym8nd

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Config {
    @Bean
    public AmazonS3 s3client(){
        AWSCredentials credentials = new BasicAWSCredentials("YCAJERDXdfQtVXuUKIkCo_Frp", "YCPdKrgWtNVOHcPLo2fPd6viNjHacD1dVWBHSQJs");

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net","ru-central1"
                        )
                )
                .build();
    }
}
