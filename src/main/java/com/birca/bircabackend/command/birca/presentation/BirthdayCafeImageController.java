package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.birca.application.BirthdayCafeImageFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BirthdayCafeImageController {

    private final BirthdayCafeImageFacade birthdayCafeImageFacade;

    @PostMapping("/v1/birthday-cafes/{birthdayCafeId}/images")
    @RequiredLogin
    public ResponseEntity<Void> uploadImage(@PathVariable Long birthdayCafeId,
                                            @ModelAttribute MultipartFile birthdayCafeImage) {
        birthdayCafeImageFacade.save(birthdayCafeId, birthdayCafeImage);
        return ResponseEntity.ok().build();
    }
}
