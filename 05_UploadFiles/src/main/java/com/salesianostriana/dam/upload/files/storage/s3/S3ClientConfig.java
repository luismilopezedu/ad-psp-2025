package com.salesianostriana.dam.upload.files.storage.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3ClientConfig {

    @Bean
    public S3Client s3Client(
            @Value("${storage.s3.region}") String region,
            @Value("${storage.s3.access-key}") String accessKey,
            @Value("${storage.s3.secret-key}") String secretKey,
            @Value("${storage.s3.path-style-access:true}") boolean pathStyleAccess,
            @Value("${storage.s3.endpoint:}") String endpoint
    ) {
        var creds = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));

        var s3cfg = S3Configuration.builder()
                .pathStyleAccessEnabled(pathStyleAccess)
                .build();

        var builder = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(creds)
                .serviceConfiguration(s3cfg);

        // Para MinIO/AiStore (S3 compatible)
        if (endpoint != null && !endpoint.isBlank()) {
            builder = builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }

    // Opcional (si quieres URL de lectura prefirmada)
    @Bean
    public S3Presigner s3Presigner(
            @Value("${storage.s3.region}") String region,
            @Value("${storage.s3.access-key}") String accessKey,
            @Value("${storage.s3.secret-key}") String secretKey,
            @Value("${storage.s3.endpoint:}") String endpoint
    ) {
        var creds = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));

        var builder = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(creds);

        if (endpoint != null && !endpoint.isBlank()) {
            builder = builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }
}

