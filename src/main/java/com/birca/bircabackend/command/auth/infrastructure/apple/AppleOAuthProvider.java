package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.common.ApiResponseExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppleOAuthProvider implements OAuthProvider {

    private static final String PROVIDER_NAME = "apple";

    private final AppleAuthApi appleAuthApi;
    private final AppleClaimValidator appleClaimValidator;

    @Override
    public OAuthMember getOAuthMember(String accessToken) {
        AppleKeyResponse keyResponse = ApiResponseExtractor.getBody(appleAuthApi.getKey());
        List<ApplePubKey> keys = keyResponse.keys();
        AppleIdentityToken identityToken = AppleIdentityToken.of(accessToken, keys);
        return identityToken.toOAuthMember(appleClaimValidator);
    }

    @Override
    public String getProvider() {
        return PROVIDER_NAME;
    }
}
