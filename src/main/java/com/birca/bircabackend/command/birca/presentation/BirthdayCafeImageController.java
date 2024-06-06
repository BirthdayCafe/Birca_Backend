package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.birca.application.BirthdayCafeImageFacade;
import com.birca.bircabackend.command.birca.dto.BirthdayCafeImageDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BirthdayCafeImageController {

    private final BirthdayCafeImageFacade birthdayCafeImageFacade;

    @PostMapping("/v1/birthday-cafes/{birthdayCafeId}/images")
    @RequiredLogin
    public ResponseEntity<Void> uploadDefaultImage(@PathVariable Long birthdayCafeId,
                                                   @ModelAttribute List<MultipartFile> defaultImages) {
        birthdayCafeImageFacade.saveDefaultImage(birthdayCafeId, defaultImages);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/birthday-cafes/{birthdayCafeId}/images/main")
    @RequiredLogin
    public ResponseEntity<Void> uploadMainImage(@PathVariable Long birthdayCafeId,
                                                @ModelAttribute MultipartFile mainImage) {
        birthdayCafeImageFacade.updateMainImage(birthdayCafeId, mainImage);
        return ResponseEntity.ok().build();
    }
}
