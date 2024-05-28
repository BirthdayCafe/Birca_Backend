package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.command.cafe.domain.CafeImageRepository;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.birca.bircabackend.command.cafe.exception.CafeImageErrorCode.INVALID_UPLOAD_SIZE_REQUEST;

@Component
@RequiredArgsConstructor
public class CafeImageValidator {

    private static final int MAX_SIZE = 5;
    private final CafeImageRepository cafeImageRepository;

    public void validateImageSize(List<MultipartFile> cafeImages, Long cafeId) {
        List<String> savedCafeImages = cafeImageRepository.findByCafeId(cafeId);
        if (cafeImages.size() + savedCafeImages.size() >= MAX_SIZE) {
            throw BusinessException.from(INVALID_UPLOAD_SIZE_REQUEST);
        }
    }
}
