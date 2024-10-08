package com.birca.bircabackend.command.auth.application.token;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

public class JwtTokenExtractor {

    private static final String BEARER_TYPE = "Bearer";

    public static Optional<String> extractToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (existBearerToken(token)) {
            return Optional.of(token.substring(BEARER_TYPE.length()).trim());
        }
        return Optional.empty();
    }

    private static boolean existBearerToken(String token) {
        return token != null
                && token.toLowerCase().startsWith(BEARER_TYPE.toLowerCase());
    }
}
