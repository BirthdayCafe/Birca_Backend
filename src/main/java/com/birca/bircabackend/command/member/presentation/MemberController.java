package com.birca.bircabackend.command.member.presentation;

import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.auth.login.RequiredLogin;
import com.birca.bircabackend.command.member.application.MemberService;
import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/v1/members/role-change")
    @RequiredLogin
    public ResponseEntity<Void> changeMemberRole(@RequestBody RoleChangeRequest request,
                                                 LoginMember loginMember) {
        memberService.changeMemberRole(request, loginMember);
        return ResponseEntity.ok().build();
    }
}
