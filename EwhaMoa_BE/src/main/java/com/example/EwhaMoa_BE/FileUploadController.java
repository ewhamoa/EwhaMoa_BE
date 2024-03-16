package com.example.EwhaMoa_BE;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/upload/image")
@RequiredArgsConstructor
public class FileUploadController {
    private final AmazonS3 amazonS3;
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String key = fileName;
            TransferManager transferManager = new TransferManager(amazonS3);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentDisposition("inline"); // 이 부분을 추가하여 페이지에 이미지를 표시
            PutObjectRequest request = new PutObjectRequest(bucket, key, file.getInputStream(), metadata);
            Upload upload = transferManager.upload(request);
            upload.waitForCompletion();
            String fileUrl = amazonS3.getUrl(bucket, key).toString();
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            logger.error("Error uploading file: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}