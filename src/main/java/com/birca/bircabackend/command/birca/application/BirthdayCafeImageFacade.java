package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImageValidator;
import com.birca.bircabackend.command.birca.dto.BirthdayCafeImageDeleteRequest;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BirthdayCafeImageFacade {

    private final BirthdayCafeImageValidator birthdayCafeImageValidator;
    private final BirthdayCafeImageService birthdayCafeImageService;
    private final ImageRepository imageRepository;
    private final EntityUtil entityUtil;

    public void saveDefaultImage(Long birthdayCafeId, List<MultipartFile> defaultImages) {
        birthdayCafeImageValidator.validateImagesSize(birthdayCafeId);
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        delete(birthdayCafeId);
        for (MultipartFile defaultImage : defaultImages) {
            String imageUrl = imageRepository.upload(defaultImage);
            birthdayCafeImageService.saveDefaultImage(birthdayCafeId, imageUrl);
        }
    }

    public void updateMainImage(Long birthdayCafeId, MultipartFile mainImage) {
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        String imageUrl = imageRepository.upload(mainImage);
        birthdayCafeImageService.updateMainImage(birthdayCafeId, imageUrl)
                .ifPresent(imageRepository::delete);
    }

    private void delete(Long birthdayCafeId) {
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        List<String> imageUrls = birthdayCafeImageService.delete(birthdayCafeId);
        for (String imageUrl : imageUrls) {
            imageRepository.delete(imageUrl);
        }
    }
}
