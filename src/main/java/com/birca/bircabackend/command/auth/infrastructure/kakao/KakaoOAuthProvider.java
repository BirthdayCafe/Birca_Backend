package com.birca.bircabackend.command.auth.infrastructure.kakao;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.common.ApiResponseExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOAuthProvider implements OAuthProvider {

    private static final String PROVIDER_NAME = "kakao";
    private static final String BEARER = "Bearer ";

    private final KakaoAuthApi kakaoAuthApi;

    @Override
    public OAuthMember getOAuthMember(String accessToken) {
        KakaoUserResponse response = callKakaoApi(accessToken);
        return new OAuthMember(
                response.id(),
                response.kakao_account().email(),
                PROVIDER_NAME
        );
    }

    private KakaoUserResponse callKakaoApi(String accessToken) {
        String bearerToken = BEARER + accessToken;
        ResponseEntity<KakaoUserResponse> response = kakaoAuthApi.getUserInfo(bearerToken);
        return ApiResponseExtractor.getBody(response);
    }

    @Override
    public String getProvider() {
        return PROVIDER_NAME;
    }
}
