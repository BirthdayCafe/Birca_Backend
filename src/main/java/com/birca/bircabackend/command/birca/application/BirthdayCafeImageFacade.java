package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.upload.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class BirthdayCafeImageFacade {

    private final BirthdayCafeImageValidator birthdayCafeImageValidator;
    private final BirthdayCafeImageService birthdayCafeImageService;
    private final ImageUploader imageUploader;
    private final EntityUtil entityUtil;

    public void updateDefaultImage(Long birthdayCafeId, MultipartFile defaultImage) {
        birthdayCafeImageValidator.validateImagesSize(birthdayCafeId);
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        String imageUrl = imageUploader.upload(defaultImage);
        birthdayCafeImageService.saveDefaultImage(birthdayCafeId, imageUrl);
    }

    public void updateMainImage(Long birthdayCafeId, MultipartFile mainImage) {
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        String imageUrl = imageUploader.upload(mainImage);
        birthdayCafeImageService.updateMainImage(birthdayCafeId, imageUrl)
                .ifPresent(imageUploader::delete);
    }
}
