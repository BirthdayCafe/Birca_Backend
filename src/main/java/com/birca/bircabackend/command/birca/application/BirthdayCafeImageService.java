package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeImageService {

    private final BirthdayCafeImageRepository birthdayCafeImageRepository;

    public void saveDefaultImage(Long birthdayCafeId, String imageUrl) {
        BirthdayCafeImage birthdayCafeImage = BirthdayCafeImage.createDefaultImage(birthdayCafeId, imageUrl);
        birthdayCafeImageRepository.save(birthdayCafeImage);
    }

    public Optional<String> updateMainImage(Long birthdayCafeId, String imageUrl) {
        return birthdayCafeImageRepository.findByBirthdayCafeIdAndIsMain(birthdayCafeId, true)
                .stream()
                .findFirst()
                .map(mainImage -> {
                    String previousImageUrl = mainImage.getImageUrl();
                    mainImage.updateMainImage(imageUrl);
                    return Optional.ofNullable(previousImageUrl);
                })
                .orElseGet(() -> {
                    saveMainImage(birthdayCafeId, imageUrl);
                    return Optional.empty();
                });
    }

    private void saveMainImage(Long birthdayCafeId, String imageUrl) {
        BirthdayCafeImage mainImage = BirthdayCafeImage.createMainImage(birthdayCafeId, imageUrl);
        birthdayCafeImageRepository.save(mainImage);
    }
}
