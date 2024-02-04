package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authentication.RequiredLogin;
import com.birca.bircabackend.query.dto.NicknameCheckResponse;
import com.birca.bircabackend.query.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/v1/join/check-nickname")
    @RequiredLogin
    public ResponseEntity<NicknameCheckResponse> checkNicknameDuplicated(@RequestParam(name = "nickname") String nickname) {
        return ResponseEntity.ok(memberQueryService.checkNickname(nickname));
    }
}
