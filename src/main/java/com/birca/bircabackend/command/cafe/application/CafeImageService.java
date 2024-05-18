package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.CafeImage;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.command.cafe.domain.CafeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.birca.bircabackend.command.cafe.exception.CafeImageErrorCode.INVALID_UPLOAD_SIZE_REQUEST;

@Service
@Transactional
@RequiredArgsConstructor
public class CafeImageService {

    private static final int MAX_SIZE = 5;

    private final CafeImageRepository cafeImageRepository;

    public void save(Long cafeId, String imageUrl) {
        validateImageSize(cafeId);
        CafeImage cafeImage = new CafeImage(cafeId, imageUrl);
        cafeImageRepository.save(cafeImage);
    }

    private void validateImageSize(Long cafeId) {
        List<String> images = cafeImageRepository.findByCafeId(cafeId);
        if (images.size() >= MAX_SIZE) {
            throw BusinessException.from(INVALID_UPLOAD_SIZE_REQUEST);
        }
    }

    public void delete(String imageUrl) {
        cafeImageRepository.deleteByImageUrl(imageUrl);
    }
}
