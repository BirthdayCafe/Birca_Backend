package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.application.token.JwtParser;
import com.birca.bircabackend.common.ApiResponseExtractor;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleOAuthProvider implements OAuthProvider {

    private static final String PROVIDER_NAME = "apple";

    private final AppleAuthApi appleAuthApi;
    private final JwtParser jwtParser;
    private final AppleClaimValidator appleClaimValidator;

    @Override
    public OAuthMember getOAuthMember(String accessToken) {
        AppleKeyResponse keyResponse = ApiResponseExtractor.getBody(appleAuthApi.getKey());
        ApplePubKeys applePubKeys = ApplePubKeys.from(keyResponse.keys());
        Claims claims = getAppleClaims(accessToken, applePubKeys);
        validateClaims(claims);
        return new OAuthMember(
                claims.getSubject(),
                (String) claims.get("email"),
                PROVIDER_NAME
        );
    }

    private Claims getAppleClaims(String accessToken, ApplePubKeys keys) {
        Map<String, String> header = jwtParser.parseHeader(accessToken);
        PublicKey publicKey = keys.findPublicKeyOf(header.get("kid"), header.get("alg"));
        return jwtParser.parseClaims(accessToken, publicKey)
                .getBody();
    }

    private void validateClaims(Claims claims) {
        if (!appleClaimValidator.isValid(claims)) {
            throw BusinessException.from(new InternalServerErrorCode("Claims이 올바르지 않습니다."));
        }
    }

    @Override
    public String getProvider() {
        return PROVIDER_NAME;
    }
}
