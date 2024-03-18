package com.birca.bircabackend.common.upload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Uploader implements ImageUploader {

    private static final String DEFAULT_PATH = "https://birca-bucket.s3.ap-northeast-2.amazonaws.com/";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public String upload(MultipartFile image) {
        try {
            String s3FileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(image.getInputStream().available());
            amazonS3.putObject(bucket, s3FileName, image.getInputStream(), objectMetadata);
            return amazonS3.getUrl(bucket, s3FileName).toString();
        } catch (Exception e) {
            log.error("S3 업로드 중 에러가 발생했습니다.");
            throw BusinessException.from(new InternalServerErrorCode(e.getMessage()));
        }
    }

    @Override
    public void delete(String imageUrl) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, getKey(imageUrl)));
    }

    private String getKey(String imageUrl) {
        String key = imageUrl.replaceAll(DEFAULT_PATH, "");
        return URLDecoder.decode(key, StandardCharsets.UTF_8);
    }
}
