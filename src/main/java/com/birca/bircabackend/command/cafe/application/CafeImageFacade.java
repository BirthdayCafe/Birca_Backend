package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.dto.CafeImageDeleteRequest;
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

    private final CafeImageService cafeImageService;
    private final ImageRepository imageRepository;
    private final EntityUtil entityUtil;

    public void uploadCafeImage(List<MultipartFile> cafeImages, Long cafeId) {
        entityUtil.getEntity(Cafe.class, cafeId, CafeErrorCode.NOT_FOUND);
        for (MultipartFile cafeImage : cafeImages) {
            String imageUrl = imageRepository.upload(cafeImage);
            cafeImageService.save(cafeId, imageUrl);
        }
    }

    public void deleteCafeImage(Long cafeId, CafeImageDeleteRequest request) {
        entityUtil.getEntity(Cafe.class, cafeId, CafeErrorCode.NOT_FOUND);
        String imageUrl = request.imageUrl();
        cafeImageService.delete(imageUrl);
        imageRepository.delete(imageUrl);
    }
}
