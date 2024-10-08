package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.query.dto.NicknameCheckResponse;
import com.birca.bircabackend.query.dto.ProfileResponse;
import com.birca.bircabackend.query.dto.RoleResponse;
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

    @GetMapping("/v1/members/me")
    @RequiredLogin
    public ResponseEntity<ProfileResponse> getMyProfile(LoginMember loginMember) {
        return ResponseEntity.ok(memberQueryService.getMyProfile(loginMember));
    }

    @GetMapping("/v1/members/role")
    @RequiredLogin
    public ResponseEntity<RoleResponse> getMyRole(LoginMember loginMember) {
        return ResponseEntity.ok(memberQueryService.getMyRole(loginMember));
    }
}
