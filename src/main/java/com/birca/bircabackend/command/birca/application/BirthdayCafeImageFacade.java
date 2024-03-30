package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.dto.BirthdayCafeImageDeleteRequest;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class BirthdayCafeImageFacade {

    private final BirthdayCafeImageValidator birthdayCafeImageValidator;
    private final BirthdayCafeImageService birthdayCafeImageService;
    private final ImageRepository imageRepository;
    private final EntityUtil entityUtil;

    public void saveDefaultImage(Long birthdayCafeId, MultipartFile defaultImage) {
        birthdayCafeImageValidator.validateImagesSize(birthdayCafeId);
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        String imageUrl = imageRepository.upload(defaultImage);
        birthdayCafeImageService.saveDefaultImage(birthdayCafeId, imageUrl);
    }

    public void updateMainImage(Long birthdayCafeId, MultipartFile mainImage) {
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        String imageUrl = imageRepository.upload(mainImage);
        birthdayCafeImageService.updateMainImage(birthdayCafeId, imageUrl)
                .ifPresent(imageRepository::delete);
    }

    public void delete(Long birthdayCafeId, BirthdayCafeImageDeleteRequest request) {
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        String imageUrl = request.imageUrl();
        birthdayCafeImageService.delete(imageUrl);
        imageRepository.delete(imageUrl);
    }
}
