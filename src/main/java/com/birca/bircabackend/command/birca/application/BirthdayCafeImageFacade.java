package com.birca.bircabackend.command.birca.application;

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

    public void save(Long birthdayCafeId, MultipartFile birthdayCafeImage) {
        birthdayCafeImageValidator.validateImagesSize(birthdayCafeId);
        //entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        String imageUrl = imageUploader.upload(birthdayCafeImage);
        birthdayCafeImageService.save(birthdayCafeId, imageUrl);
    }
}
