package com.birca.bircabackend.command.member.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.domain.*;
import com.birca.bircabackend.command.member.dto.NicknameRegisterRequest;
import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.member.exception.MemberErrorCode.DUPLICATED_NICKNAME;
import static com.birca.bircabackend.command.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EntityUtil entityUtil;

    public void join(Member member) {
        validateDuplicatedIdentityKey(member.getIdentityKey());
        memberRepository.save(member);
    }

    private void validateDuplicatedIdentityKey(IdentityKey identityKey) {
        if (memberRepository.existsByIdentityKey(identityKey)) {
            throw BusinessException.from(MemberErrorCode.DUPLICATED_IDENTITY_KEY);
        }
    }

    public void changeMemberRole(RoleChangeRequest request, LoginMember loginMember) {
        Member member = entityUtil.getEntity(Member.class, loginMember.id(), MEMBER_NOT_FOUND);
        MemberRole role = MemberRole.from(request.role());
        member.changeRole(role);
    }

    public void registerNickname(NicknameRegisterRequest request, LoginMember loginMember) {
        Nickname nickname = new Nickname(request.nickname());
        validateDuplicatedNickname(nickname);
        Member member = entityUtil.getEntity(Member.class, loginMember.id(), MEMBER_NOT_FOUND);
        member.registerNickname(nickname);
    }

    private void validateDuplicatedNickname(Nickname nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw BusinessException.from(DUPLICATED_NICKNAME);
        }
    }

    public void withdrawMember(LoginMember loginMember) {
        Member member = entityUtil.getEntity(Member.class, loginMember.id(), MEMBER_NOT_FOUND);
        member.withdrawMember();
    }
}
