package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImageBulkSaveRepository;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.upload.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.INVALID_UPLOAD_SIZE_REQUEST;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeImageService {

    private static final int MAX_SIZE = 10;

    private final BirthdayCafeImageBulkSaveRepository birthdayCafeImageBulkSaveRepository;
    private final ImageUploader imageUploader;
    private final EntityUtil entityUtil;

    public void save(Long birthdayCafeId, List<MultipartFile> images) {
        validateImagesSize(images);
        entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        List<BirthdayCafeImage> birthdayCafeImages = images.stream()
                .map(image -> new BirthdayCafeImage(birthdayCafeId, imageUploader.upload(image)))
                .toList();
        birthdayCafeImageBulkSaveRepository.saveAll(birthdayCafeImages);
    }

    private static void validateImagesSize(List<MultipartFile> images) {
        if (images.size() > MAX_SIZE) {
            throw BusinessException.from(INVALID_UPLOAD_SIZE_REQUEST);
        }
    }
}
