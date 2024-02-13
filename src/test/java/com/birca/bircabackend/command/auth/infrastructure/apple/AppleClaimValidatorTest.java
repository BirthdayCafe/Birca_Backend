package com.birca.bircabackend.command.auth.infrastructure.apple;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AppleClaimValidatorTest {

    private static final String ISS = "iss";
    private static final String CLIENT_ID = "aud";
    private static final String NONCE = "nonce";
    private static final String NONCE_KEY = "nonce";

    private final AppleClaimValidator appleClaimValidator = new AppleClaimValidator(ISS, CLIENT_ID, NONCE);

    @Nested
    @DisplayName("애플 claim 검증 시")
    class ValidTes {

        @Test
        void iss_client_id_nonce_모두_맞으면_true_반환한다() {
            // given
            Claims claims = Jwts.claims(Map.of(NONCE_KEY, EncryptUtil.encrypt(NONCE)))
                    .setIssuer(ISS)
                    .setAudience(CLIENT_ID);

            // when
            boolean actual = appleClaimValidator.isValid(claims);

            // then
            assertThat(actual).isTrue();
        }

        @ParameterizedTest
        @CsvSource({
                "X, iss, aud",
                "nonce, X, aud",
                "nonce, iss, X",
                "X, X, X"
        })
        void iss_client_id_nonce_하나라도_틀리면_false_반환한다(String nonce, String iss, String clientId) {
            // given
            Claims claims = Jwts.claims(Map.of(NONCE_KEY, EncryptUtil.encrypt(nonce)))
                    .setIssuer(iss)
                    .setAudience(clientId);

            // when
            boolean actual = appleClaimValidator.isValid(claims);

            // then
            assertThat(actual).isFalse();
        }
    }
}