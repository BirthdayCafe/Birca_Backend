package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.command.member.domain.Nickname;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.query.dto.NicknameCheckResponse;
import com.birca.bircabackend.query.dto.ProfileResponse;
import com.birca.bircabackend.query.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.member.exception.MemberErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;
    private final EntityUtil entityUtil;

    public NicknameCheckResponse checkNickname(String nickname) {
        Boolean isDuplicated = memberQueryRepository.existsByNickname(new Nickname(nickname));
        return new NicknameCheckResponse(isDuplicated);
    }

    public ProfileResponse getMyProfile(LoginMember loginMember) {
        Member member = entityUtil.getEntity(Member.class, loginMember.id(), MEMBER_NOT_FOUND);
        String nickname = member.getNickname().getValue();
        return new ProfileResponse(nickname);
    }
}
