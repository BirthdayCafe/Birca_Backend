package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.upload.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BirthdayCafeImageFacade {

    private final BirthdayCafeImageValidator birthdayCafeImageValidator;
    private final BirthdayCafeImageService birthdayCafeImageService;
    private final ImageUploader imageUploader;
    private final EntityUtil entityUtil;

    public void save(Long birthdayCafeId, List<MultipartFile> images) {
        birthdayCafeImageValidator.validateImagesSize(images);
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        List<String> imagesUrl = images.stream()
                .map(imageUploader::upload)
                .toList();
        birthdayCafeImageService.save(birthdayCafeId, imagesUrl);
    }

}
