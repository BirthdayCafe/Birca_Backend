package com.birca.bircabackend.command.auth.application.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "${jwt.token.secret-key}";
    private static final String EXPIRE_LENGTH = "${jwt.token.expire-length}";

    private final SecretKey key;
    private final long validityInMilliseconds;

    public JwtTokenProvider(@Value(SECRET_KEY) final String secretKey,
                            @Value(EXPIRE_LENGTH) final long validityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(TokenPayload payload) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .addClaims(payload.toMap())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenPayload getPayload(String token) {
        Claims claims = JwtParseUtil.parseClaims(token, key).getBody();
        Long id = claims.get("id", Long.class);
        return new TokenPayload(id);
    }

    public boolean isValidAccessToken(String accessToken) {
        try {
            return !JwtParseUtil.parseClaims(accessToken, key)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

