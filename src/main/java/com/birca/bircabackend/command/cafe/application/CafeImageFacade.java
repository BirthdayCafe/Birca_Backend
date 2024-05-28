package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.domain.CafeImageValidator;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CafeImageFacade {

    private final CafeImageValidator cafeImageValidator;
    private final CafeImageService cafeImageService;
    private final ImageRepository imageRepository;
    private final EntityUtil entityUtil;

    public void uploadCafeImage(List<MultipartFile> cafeImages, Long cafeId) {
        entityUtil.getEntity(Cafe.class, cafeId, CafeErrorCode.NOT_FOUND);
        deleteCafeImages(cafeId);
        cafeImageValidator.validateImageSize(cafeImages, cafeId);
        for (MultipartFile cafeImage : cafeImages) {
            String imageUrl = imageRepository.upload(cafeImage);
            cafeImageService.save(cafeId, imageUrl);
        }
    }

    private void deleteCafeImages(Long cafeId) {
        entityUtil.getEntity(Cafe.class, cafeId, CafeErrorCode.NOT_FOUND);
        List<String> cafeImageUrls = cafeImageService.delete(cafeId);
        for (String cafeImageUrl : cafeImageUrls) {
            imageRepository.delete(cafeImageUrl);
        }
    }
}
