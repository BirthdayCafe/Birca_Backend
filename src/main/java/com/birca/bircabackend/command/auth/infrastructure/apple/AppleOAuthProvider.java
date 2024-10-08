package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.infrastructure.OAuthConst;
import com.birca.bircabackend.common.ApiResponseExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppleOAuthProvider implements OAuthProvider {

    private final AppleAuthApi appleAuthApi;

    @Override
    public OAuthMember getOAuthMember(String accessToken) {
        AppleKeyResponse keyResponse = ApiResponseExtractor.getBody(appleAuthApi.getKey());
        List<ApplePubKey> keys = keyResponse.keys();
        AppleIdentityToken identityToken = AppleIdentityToken.of(accessToken, keys);
        return identityToken.toOAuthMember();
    }

    @Override
    public String getProvider() {
        return OAuthConst.APPLE.getName();
    }
}
