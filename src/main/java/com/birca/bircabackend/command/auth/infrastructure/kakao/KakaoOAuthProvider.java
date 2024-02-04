package com.birca.bircabackend.command.auth.infrastructure.kakao;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
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
        String bearerToken = generateBearerToken(accessToken);
        ResponseEntity<KakaoUserResponse> response = kakaoAuthApi.getUserInfo(bearerToken);
        handleApiCallFail(response);
        return new OAuthMember(extractEmail(response), PROVIDER_NAME);
    }

    private String generateBearerToken(String accessToken) {
        return BEARER + accessToken;
    }

    private String extractEmail(ResponseEntity<KakaoUserResponse> response) {
        return response.getBody().kakaoAccount().email();
    }

    private void handleApiCallFail(ResponseEntity<KakaoUserResponse> response) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw BusinessException.from(new InternalServerErrorCode("카카오 api 호출에 문제가 생겼습니다."));
        }
    }

    @Override
    public String getProvider() {
        return PROVIDER_NAME;
    }
}
