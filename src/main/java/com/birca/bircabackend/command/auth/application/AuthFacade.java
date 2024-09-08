package com.birca.bircabackend.command.auth.application;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProviderFactory;
import com.birca.bircabackend.command.auth.dto.LoginRequest;
import com.birca.bircabackend.command.auth.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final OAuthProviderFactory providerFactory;
    private final AuthService authService;

    public LoginResponse login(LoginRequest request, String provider) {
        OAuthProvider oAuthProvider = providerFactory.getProvider(provider);
        OAuthMember oAuthMember = oAuthProvider.getOAuthMember(request.accessToken());
        return authService.login(oAuthMember);
    }
}
