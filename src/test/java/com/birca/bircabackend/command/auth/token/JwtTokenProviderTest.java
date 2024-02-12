package com.birca.bircabackend.command.auth.token;

import com.birca.bircabackend.command.auth.application.token.JwtTokenProvider;
import com.birca.bircabackend.command.auth.application.token.TokenPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private final JwtTokenProvider tokenProvider = new JwtTokenProvider(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ6.eyJzdWIiOiIiLCJuYW1lIjoiSm3obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt23Lno",
            1800000);

    private final TokenPayload payload = new TokenPayload(1L);

    @Test
    void JWT_토큰을_생성한다() {
        // when
        String actual = tokenProvider.createToken(payload);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void JWT_토큰에서_payload를_추출한다() {
        // given
        String token = tokenProvider.createToken(payload);

        // when
        TokenPayload actual = tokenProvider.getPayload(token);

        // then
        assertThat(actual).isEqualTo(payload);
    }

    @Nested
    @DisplayName("JWT 토큰이")
    class ValidAccessTokenTest {

        @Test
        void 유효한_경우_True를_반환한다() {
            // given
            String token = tokenProvider.createToken(payload);

            // when
            boolean actual = tokenProvider.isValidAccessToken(token);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 유효하지_않은_경우_False를_반환한다() {
            // given
            String token = "notValidToken.notValidToken.notValidToken";

            // when
            boolean actual = tokenProvider.isValidAccessToken(token);

            // then
            assertThat(actual).isFalse();
        }
    }
}
