package com.birca.bircabackend.common;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class JwtParser {

    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final String TOKEN_HEADER_DELIMITER = ".";
    private static final int HEADER_INDEX = 0;

    private final ObjectMapper objectMapper;

    public Map<String, String> parseHeader(String token) {
        String headerOfToken = token.substring(HEADER_INDEX, token.indexOf(TOKEN_HEADER_DELIMITER));
        byte[] decodedHeaderOfToken = BASE64_DECODER.decode(headerOfToken);
        try {
            return objectMapper.readValue(new String(decodedHeaderOfToken, UTF_8), Map.class);
        } catch (JsonProcessingException e) {
            throw BusinessException.from(new InternalServerErrorCode(e.getMessage()));
        }
    }
}
