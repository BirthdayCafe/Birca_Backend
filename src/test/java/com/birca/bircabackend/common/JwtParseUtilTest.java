package com.birca.bircabackend.common;

import com.birca.bircabackend.command.auth.application.token.JwtParseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtParseUtilTest {

    private static final String CLAIM_KEY = "claimKey";
    private static final String CLAIM_BODY = "body";

    private String token;
    private Key privateKey;

    @BeforeEach
    void initToken() throws Exception {
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA")
                .generateKeyPair();
        privateKey = keyPair.getPrivate();
        token = Jwts.builder()
                .setHeaderParam("kid", "W2R4HXF3K")
                .addClaims(Map.of(CLAIM_KEY, CLAIM_BODY))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Test
    void 토큰_헤더를_파싱한다() {
        // when
        Map<String, String> actual = JwtParseUtil.parseHeader(token);

        // then
        assertThat(actual).containsOnlyKeys("alg", "kid");
    }

    @Test
    void 토큰_클레임을_파싱한다() {
        // when
        Jws<Claims> claims = JwtParseUtil.parseClaims(token, privateKey);

        // then
        assertThat(claims.getBody().get(CLAIM_KEY)).isEqualTo(CLAIM_BODY);
    }
}
