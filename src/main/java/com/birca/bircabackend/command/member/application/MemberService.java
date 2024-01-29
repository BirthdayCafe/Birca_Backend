package com.birca.bircabackend.command.member.application;

import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class MemberService {

    public void changeMemberRole(RoleChangeRequest request, LoginMember loginMember) {

    }
}
