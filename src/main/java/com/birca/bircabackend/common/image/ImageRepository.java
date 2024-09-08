package com.birca.bircabackend.common.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {

    String upload(MultipartFile image);

    void delete(String imageUrl);
}
