package com.birca.bircabackend.command.member.application;

import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.command.member.domain.MemberRepository;
import com.birca.bircabackend.command.member.domain.MemberRole;
import com.birca.bircabackend.command.member.dto.NicknameRegisterRequest;
import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void changeMemberRole(RoleChangeRequest request, LoginMember loginMember) {
        Member member = getMember(loginMember);
        MemberRole role = MemberRole.from(request.role());
        member.changeRole(role);
    }

    public void registerNickname(NicknameRegisterRequest request, LoginMember loginMember) {
        validateDuplicatedNickname(request);
        Member member = getMember(loginMember);
        member.registerNickname(request.nickname());
    }

    private void validateDuplicatedNickname(NicknameRegisterRequest request) {
        if (memberRepository.existsByNickname(request.nickname())) {
            throw BusinessException.from(MemberErrorCode.DUPLICATED_NICKNAME);
        }
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.id())
                .orElseThrow(() -> BusinessException.from(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
