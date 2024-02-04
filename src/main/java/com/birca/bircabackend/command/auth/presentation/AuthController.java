package com.birca.bircabackend.command.auth.presentation;

import com.birca.bircabackend.command.auth.application.AuthService;
import com.birca.bircabackend.command.auth.dto.LoginRequest;
import com.birca.bircabackend.command.auth.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/oauth/login/{provider}")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
                                               @PathVariable(name = "provider") String provider) {
        return ResponseEntity.ok(authService.login(request, provider));
    }
}
