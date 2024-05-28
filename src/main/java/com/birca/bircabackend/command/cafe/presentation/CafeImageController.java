package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.cafe.application.CafeImageFacade;
import com.birca.bircabackend.command.cafe.dto.CafeImageDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeImageController {

    private final CafeImageFacade cafeImageFacade;

    @PostMapping("/v1/cafes/{cafeId}/images")
    @RequiredLogin
    public ResponseEntity<Void> uploadCafeImage(@ModelAttribute List<MultipartFile> cafeImages,
                                                @PathVariable Long cafeId) {
        cafeImageFacade.uploadCafeImage(cafeImages, cafeId);
        return ResponseEntity.ok().build();
    }
}
