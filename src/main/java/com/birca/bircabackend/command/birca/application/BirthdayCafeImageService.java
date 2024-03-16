package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeImageService {

    private final BirthdayCafeImageRepository birthdayCafeImageRepository;

    public void save(Long birthdayCafeId, String imageUrl) {
        BirthdayCafeImage birthdayCafeImage = new BirthdayCafeImage(birthdayCafeId, imageUrl);
        birthdayCafeImageRepository.save(birthdayCafeImage);
    }
}
