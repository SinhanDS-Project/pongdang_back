package com.wepong.pongdang.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AmazonS3Config {

    @Value("${amazon.access-key}")
    private String accessKey;
    
    @Value("${amazon.secret-key}")
    private String secretKey;
    
    @Value("${amazon.region}")
    private String region;
    
    @Value("${amazon.bucket-name}")
    private String bucketName;

	@Bean
    public AmazonS3Client amazonS3Client() {
        // AWS 자격증명 객체 생성
    	AWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        // Amazon S3 클라이언트 빌드 (지정한 리전으로)
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region)
                .build();
    }
}

