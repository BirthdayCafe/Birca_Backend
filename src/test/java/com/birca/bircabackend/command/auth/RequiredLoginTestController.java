package com.birca.bircabackend.command.auth;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequiredLoginTestController {

    @GetMapping("/test-login")
    @RequiredLogin
    public ResponseEntity<Long> requireLogin(LoginMember loginMember) {
        return ResponseEntity.ok(loginMember.id());
    }
}
