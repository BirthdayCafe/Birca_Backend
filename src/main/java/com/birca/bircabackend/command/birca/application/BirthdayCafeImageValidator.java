package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImageRepository;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.INVALID_UPLOAD_SIZE_REQUEST;

@Component
@RequiredArgsConstructor
public class BirthdayCafeImageValidator {

    private static final int MAX_SIZE = 10;

    private final BirthdayCafeImageRepository birthdayCafeImageRepository;

    public void validateImagesSize(Long birthdayCafeId) {
        List<BirthdayCafeImage> registeredImages = birthdayCafeImageRepository.findDefaultByBirthdayCafeId(birthdayCafeId);
        if (registeredImages.size() >= MAX_SIZE) {
            throw BusinessException.from(INVALID_UPLOAD_SIZE_REQUEST);
        }
    }
}
