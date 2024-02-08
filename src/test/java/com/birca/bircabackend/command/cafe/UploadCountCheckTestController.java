package com.birca.bircabackend.command.cafe;

import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.cafe.validation.UploadCountCheck;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadCountCheckTestController {

    @PostMapping("/test-upload-count")
    @RequiredLogin
    @UploadCountCheck
    public ResponseEntity<Void> uploadCountCheck() {
        return ResponseEntity.ok().build();
    }
}
