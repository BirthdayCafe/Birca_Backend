package com.birca.bircabackend.command.auth.infrastructure;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuthProvider implements OAuthProvider {

    private static final String PROVIDER_NAME = "kakao";

    @Override
    public OAuthMember getOAuthMember(String accessToken) {
        return null;
    }

    @Override
    public String getProvider() {
        return PROVIDER_NAME;
    }
}
