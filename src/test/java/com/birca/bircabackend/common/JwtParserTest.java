package com.birca.bircabackend.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtParserTest {

    private final JwtParser jwtParser = new JwtParser(new ObjectMapper());

    @Test
    void 토큰_헤더를_파싱한다() throws Exception {
        // given
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA")
                .generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        String token = Jwts.builder()
                .setHeaderParam("kid", "W2R4HXF3K")
                .addClaims(Map.of())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        // when
        Map<String, String> actual = jwtParser.parseHeader(token);

        // then
        assertThat(actual).containsOnlyKeys("alg", "kid");
    }
}
