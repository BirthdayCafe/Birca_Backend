package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImageBulkSaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeImageService {

    private final BirthdayCafeImageBulkSaveRepository birthdayCafeImageBulkSaveRepository;

    public void save(Long birthdayCafeId, List<String> imagesUrl) {
        List<BirthdayCafeImage> birthdayCafeImages = imagesUrl.stream()
                .map(imageUrl -> new BirthdayCafeImage(birthdayCafeId, imageUrl))
                .toList();
        birthdayCafeImageBulkSaveRepository.saveAll(birthdayCafeImages);
    }
}
