package com.salesianostriana.dam.upload.files.storage.s3;

import com.salesianostriana.dam.upload.files.shared.exception.StorageException;
import com.salesianostriana.dam.upload.files.shared.model.FileMetadata;
import com.salesianostriana.dam.upload.files.storage.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
public class MinioS3StorageService implements StorageService {

    private final S3Client s3;

    @Value("${storage.s3.bucket}")
    private String bucket;

    @Value("${storage.s3.public-base-url:}")
    private String publicBaseUrl; // opcional

    public MinioS3StorageService(S3Client s3) {
        this.s3 = s3;
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            boolean exists = s3.listBuckets().buckets().stream()
                    .anyMatch(b -> b.name().equals(bucket));

            if (!exists) {
                s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            }
        } catch (S3Exception e) {
            throw new StorageException("Could not initialize S3/MinIO bucket: " + bucket, e);
        }
    }

    @Override
    public FileMetadata store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("The file is empty");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );

        String key = buildObjectKey(originalFilename);

        try {
            String contentType = file.getContentType();
            if (!StringUtils.hasText(contentType)) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType)
                    // Si quieres: .metadata(Map.of("originalFilename", originalFilename))
                    .build();

            s3.putObject(putReq, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String url = buildPublicUrl(key);

            return S3FileMetadataImpl.of(
                    key,
                    originalFilename,
                    url
            );


        } catch (IOException e) {
            throw new StorageException("Error reading upload stream: " + originalFilename, e);
        } catch (S3Exception e) {
            throw new StorageException("Error storing file in S3/MinIO: " + originalFilename, e);
        }
    }

    @Override
    public Resource loadAsResource(String id) {
        try {
            var head = s3.headObject(HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(id)
                    .build());

            // (Opcional) si al hacer putObject guardaste el nombre original en metadata:
            // PutObjectRequest.builder().metadata(Map.of("original-filename", originalFilename))...
            Map<String, String> meta = head.metadata();
            String originalFilename = meta != null ? meta.get("original-filename") : null;

            String filename = StringUtils.hasText(originalFilename) ? originalFilename : id;

            String contentType = head.contentType(); // puede ser null
            long length = head.contentLength() != null ? head.contentLength() : -1L;

            Supplier<InputStream> supplier = () -> s3.getObject(GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(id)
                    .build());

            return new S3ObjectResource(supplier, filename, length, contentType);

        } catch (Exception e) {
            throw new StorageException("Could not read file: " + id, e);
        }
    }

    @Override
    public void deleteFile(String filenameOrKey) {
        try {
            DeleteObjectRequest delReq = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(filenameOrKey)
                    .build();

            s3.deleteObject(delReq);

        } catch (S3Exception e) {
            throw new StorageException("Could not delete file: " + filenameOrKey, e);
        }
    }

    private String buildObjectKey(String originalFilename) {
        // Evita colisiones + mantiene extensión si existe
        String ext = StringUtils.getFilenameExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return (StringUtils.hasText(ext)) ? (uuid + "." + ext) : uuid;
    }

    private String buildPublicUrl(String key) {
        // Si tienes un gateway público (nginx/traefik) o el propio endpoint expuesto
        if (StringUtils.hasText(publicBaseUrl)) {
            String base = publicBaseUrl.endsWith("/") ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1) : publicBaseUrl;
            return base + "/" + key;
        }
        // Si no hay URL pública, devuelves null o algo interno
        return null;
    }
}

