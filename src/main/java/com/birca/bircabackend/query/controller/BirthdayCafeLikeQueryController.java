package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.query.dto.BirthdayCafeLikeResponse;
import com.birca.bircabackend.query.service.BirthdayCafeLikeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BirthdayCafeLikeQueryController {

    private final BirthdayCafeLikeQueryService birthdayCafeLikeQueryService;

    @GetMapping("/v1/birthday-cafes/like")
    @RequiredLogin
    public ResponseEntity<List<BirthdayCafeLikeResponse>> findLikedBirthdayCafes(LoginMember loginMember) {
        return ResponseEntity.ok(birthdayCafeLikeQueryService.findLikedBirthdayCafes(loginMember.id()));
    }
}
