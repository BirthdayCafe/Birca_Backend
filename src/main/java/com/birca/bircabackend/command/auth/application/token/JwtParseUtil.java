package com.birca.bircabackend.command.auth.application.token;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtParseUtil {

    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final String TOKEN_HEADER_DELIMITER = ".";
    private static final int HEADER_INDEX = 0;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Jws<Claims> parseClaims(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public static Map<String, String> parseHeader(String token) {
        String headerOfToken = token.substring(HEADER_INDEX, token.indexOf(TOKEN_HEADER_DELIMITER));
        byte[] decodedHeaderOfToken = BASE64_DECODER.decode(headerOfToken);
        try {
            return OBJECT_MAPPER.readValue(new String(decodedHeaderOfToken, UTF_8), Map.class);
        } catch (JsonProcessingException e) {
            throw BusinessException.from(new InternalServerErrorCode(e.getMessage()));
        }
    }
}
