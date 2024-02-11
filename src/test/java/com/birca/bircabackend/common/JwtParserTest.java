package com.birca.bircabackend.common;

import com.birca.bircabackend.command.auth.application.token.JwtParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtParserTest {

    private static final String CLAIM_KEY = "claimKey";
    private static final String CLAIM_BODY = "body";

    private final JwtParser jwtParser = new JwtParser(new ObjectMapper());

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
        Map<String, String> actual = jwtParser.parseHeader(token);

        // then
        assertThat(actual).containsOnlyKeys("alg", "kid");
    }

    @Test
    void 토큰_클레임을_파싱한다() {
        // when
        Jws<Claims> claims = jwtParser.parseClaims(token, privateKey);

        // then
        assertThat(claims.getBody().get(CLAIM_KEY)).isEqualTo(CLAIM_BODY);
    }
}
