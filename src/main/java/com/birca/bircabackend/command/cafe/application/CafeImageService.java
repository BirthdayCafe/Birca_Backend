package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.CafeImage;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.command.cafe.domain.CafeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.birca.bircabackend.command.cafe.exception.CafeImageErrorCode.INVALID_UPLOAD_SIZE_REQUEST;

@Service
@Transactional
@RequiredArgsConstructor
public class CafeImageService {

    private final CafeImageRepository cafeImageRepository;

    public void save(Long cafeId, String imageUrl) {
        CafeImage cafeImage = new CafeImage(cafeId, imageUrl);
        cafeImageRepository.save(cafeImage);
    }

    public List<String> delete(Long cafeId) {
        List<String> imageUrls = cafeImageRepository.findByCafeId(cafeId);
        cafeImageRepository.deleteByCafeId(cafeId);
        return imageUrls;
    }
}
