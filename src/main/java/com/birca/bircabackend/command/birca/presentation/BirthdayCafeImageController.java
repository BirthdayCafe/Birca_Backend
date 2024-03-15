package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.birca.application.BirthdayCafeImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BirthdayCafeImageController {

    private final BirthdayCafeImageService birthdayCafeImageService;

    @PostMapping("/v1/birthday-cafes/{birthdayCafeId}/images")
    @RequiredLogin
    public ResponseEntity<Void> uploadImage(@PathVariable Long birthdayCafeId,
                                            @ModelAttribute List<MultipartFile> images) {
        birthdayCafeImageService.save(birthdayCafeId, images);
        return ResponseEntity.ok().build();
    }
}
