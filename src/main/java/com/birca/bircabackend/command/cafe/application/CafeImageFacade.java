package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class CafeImageFacade {

    private final CafeImageService cafeImageService;
    private final ImageRepository imageRepository;
    private final EntityUtil entityUtil;

    public void uploadCafeImage(MultipartFile cafeImage, Long cafeId) {
        entityUtil.getEntity(Cafe.class, cafeId, CafeErrorCode.NOT_FOUND);
        String imageUrl = imageRepository.upload(cafeImage);
        cafeImageService.save(cafeId, imageUrl);
    }
}
