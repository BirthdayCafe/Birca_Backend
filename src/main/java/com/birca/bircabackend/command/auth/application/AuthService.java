package com.birca.bircabackend.command.auth.application;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.token.JwtTokenProvider;
import com.birca.bircabackend.command.auth.application.token.TokenPayload;
import com.birca.bircabackend.command.auth.dto.LoginResponse;
import com.birca.bircabackend.command.member.application.MemberService;
import com.birca.bircabackend.command.member.domain.IdentityKey;
import com.birca.bircabackend.command.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private static final boolean EXIST_MEMBER = false;
    private static final boolean NEW_MEMBER = true;

    private final MemberAuthRepository memberAuthRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginResponse login(OAuthMember oAuthMember) {
        IdentityKey identityKey = IdentityKey.of(oAuthMember.id(), oAuthMember.provider());
        String email = oAuthMember.email();
        return memberAuthRepository.findByIdentityKey(identityKey)
                .map(member -> mapToResponse(member, EXIST_MEMBER))
                .orElseGet(() -> mapToResponse(join(email, identityKey), NEW_MEMBER));
    }

    private Member join(String email, IdentityKey identityKey) {
        Member member = Member.join(email, identityKey);
        memberService.join(member);
        return member;
    }

    private LoginResponse mapToResponse(Member member, boolean isNewMember) {
        return new LoginResponse(
                jwtTokenProvider.createToken(new TokenPayload(member.getId())),
                isNewMember,
                member.getRole().name()
        );
    }
}
