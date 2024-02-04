package com.birca.bircabackend.command.auth.application;

import com.birca.bircabackend.command.auth.application.token.JwtTokenProvider;
import com.birca.bircabackend.command.auth.application.token.TokenPayload;
import com.birca.bircabackend.command.auth.dto.LoginRequest;
import com.birca.bircabackend.command.auth.dto.LoginResponse;
import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProviderFactory;
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

    private final OAuthProviderFactory providerFactory;
    private final MemberAuthRepository memberAuthRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request, String provider) {
        OAuthProvider oAuthProvider = providerFactory.getProvider(provider);
        OAuthMember oAuthMember = oAuthProvider.getOAuthMember(request.accessToken());
        String email = oAuthMember.email();
        return memberAuthRepository.findByEmail(email)
                .map(member -> getLoginResponse(member, EXIST_MEMBER))
                .orElseGet(() -> getLoginResponse(join(email), NEW_MEMBER));
    }

    private Member join(String email) {
        return memberAuthRepository.save(new Member(email));
    }

    private LoginResponse getLoginResponse(Member member, boolean isNewMember) {
        return new LoginResponse(
                jwtTokenProvider.createToken(new TokenPayload(member.getId())),
                isNewMember,
                member.getRole().name()
        );
    }
}