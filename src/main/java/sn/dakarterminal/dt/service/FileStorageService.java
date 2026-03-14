package sn.dakarterminal.dt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    @Value("${storage.type:local}")
    private String storageType;

    @Value("${storage.local.path:./uploads}")
    private String localStoragePath;

    @Value("${storage.s3.endpoint:}")
    private String s3Endpoint;

    @Value("${storage.s3.bucket:dakar-terminal}")
    private String s3Bucket;

    @Value("${storage.s3.region:us-east-1}")
    private String s3Region;

    @Value("${storage.s3.access-key:}")
    private String s3AccessKey;

    @Value("${storage.s3.secret-key:}")
    private String s3SecretKey;

    @Value("${storage.s3.public-url:}")
    private String s3PublicUrl;

    public String store(MultipartFile file, String directory) throws IOException {
        String fileName = UUID.randomUUID() + "_" + sanitizeFileName(file.getOriginalFilename());
        String path = directory + "/" + fileName;

        if ("s3".equals(storageType)) {
            return storeS3(file, path);
        } else {
            return storeLocal(file, path);
        }
    }

    private String storeLocal(MultipartFile file, String relativePath) throws IOException {
        Path fullPath = Paths.get(localStoragePath, relativePath);
        Files.createDirectories(fullPath.getParent());
        Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
        return relativePath;
    }

    private String storeS3(MultipartFile file, String key) throws IOException {
        S3Client s3 = getS3Client();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(s3Bucket)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();
        s3.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return key;
    }

    public byte[] load(String filePath) throws IOException {
        if ("s3".equals(storageType)) {
            return loadS3(filePath);
        } else {
            return loadLocal(filePath);
        }
    }

    private byte[] loadLocal(String relativePath) throws IOException {
        Path fullPath = Paths.get(localStoragePath, relativePath);
        return Files.readAllBytes(fullPath);
    }

    private byte[] loadS3(String key) throws IOException {
        S3Client s3 = getS3Client();
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3Bucket)
                .key(key)
                .build();
        try (InputStream is = s3.getObject(request)) {
            return is.readAllBytes();
        }
    }

    public void delete(String filePath) {
        if ("s3".equals(storageType)) {
            deleteS3(filePath);
        } else {
            deleteLocal(filePath);
        }
    }

    private void deleteLocal(String relativePath) {
        try {
            Path fullPath = Paths.get(localStoragePath, relativePath);
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            log.error("Failed to delete local file: {}", relativePath, e);
        }
    }

    private void deleteS3(String key) {
        try {
            S3Client s3 = getS3Client();
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(s3Bucket)
                    .key(key)
                    .build();
            s3.deleteObject(request);
        } catch (Exception e) {
            log.error("Failed to delete S3 object: {}", key, e);
        }
    }

    public String getPublicUrl(String filePath) {
        if ("s3".equals(storageType)) {
            return s3PublicUrl + "/" + filePath;
        } else {
            return "/api/files/download?path=" + filePath;
        }
    }

    private S3Client getS3Client() {
        var builder = S3Client.builder()
                .region(Region.of(s3Region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(s3AccessKey, s3SecretKey)));

        if (s3Endpoint != null && !s3Endpoint.isBlank()) {
            builder.endpointOverride(URI.create(s3Endpoint));
        }

        return builder.build();
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null) return "file";
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
