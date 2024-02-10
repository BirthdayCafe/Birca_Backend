package com.birca.bircabackend.command.cafe.infrastructure.uploadcount;

import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OcrRequestCountTestController {

    @PostMapping("/test-upload-count")
    @RequiredLogin
    public ResponseEntity<Void> uploadCountCheck() {
        return ResponseEntity.ok().build();
    }
}
