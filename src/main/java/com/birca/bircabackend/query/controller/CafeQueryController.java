package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.query.dto.CafeDetailResponse;
import com.birca.bircabackend.query.dto.CafeResponse;
import com.birca.bircabackend.query.service.CafeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeQueryController {

    private final CafeQueryService cafeQueryService;

    @GetMapping("/v1/cafes/search")
    @RequiredLogin
    public ResponseEntity<List<CafeResponse>> searchCafes(@RequestParam String name) {
        return ResponseEntity.ok(cafeQueryService.findCafes(name));
    }

    @GetMapping("/v1/cafes/me")
    @RequiredLogin
    public ResponseEntity<CafeDetailResponse> findCafeDetails(LoginMember loginMember) {
        return ResponseEntity.ok(cafeQueryService.findCafeDetail(loginMember));
    }
}
