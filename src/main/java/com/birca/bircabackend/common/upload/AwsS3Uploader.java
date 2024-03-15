package com.birca.bircabackend.common.upload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AwsS3Uploader implements ImageUploader {

    private static final ObjectMetadata OBJECT_METADATA = new ObjectMetadata();

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public String upload(MultipartFile image) {
        try {
            String s3FileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
            OBJECT_METADATA.setContentLength(image.getInputStream().available());
            amazonS3.putObject(bucket, s3FileName, image.getInputStream(), OBJECT_METADATA);
            return amazonS3.getUrl(bucket, s3FileName).toString();
        } catch (Exception e) {
            throw BusinessException.from(new InternalServerErrorCode(e.getMessage()));
        }
    }
}
