package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.cafe.application.CafeImageFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeImageController {

    private final CafeImageFacade cafeImageFacade;

    @PostMapping("/v1/cafes/{cafeId}/images")
    @RequiredLogin
    public ResponseEntity<Void> uploadCafeImage(@ModelAttribute MultipartFile cafeImage,
                                                @PathVariable Long cafeId) {
        cafeImageFacade.uploadCafeImage(cafeImage, cafeId);
        return ResponseEntity.ok().build();
    }
}
