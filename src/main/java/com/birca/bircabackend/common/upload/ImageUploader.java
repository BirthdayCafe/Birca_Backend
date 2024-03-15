package com.birca.bircabackend.common.upload;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    String upload(MultipartFile image);
}
