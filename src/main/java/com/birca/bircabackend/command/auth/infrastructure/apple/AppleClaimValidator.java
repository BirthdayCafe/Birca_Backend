package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class AppleClaimValidator {

    private static final String ALGORITHM = "SHA-256";
    private static final String NONCE_KEY = "nonce";

    private final String iss;
    private final String clientId;
    private final String nonce;

    public AppleClaimValidator(@Value("${oauth.apple.url}") String iss,
                               @Value("${oauth.apple.client-id}") String clientId,
                               @Value("${oauth.apple.nonce}") String nonce) {
        this.iss = iss;
        this.clientId = clientId;
        this.nonce = encryptNonce(nonce);
    }

    public boolean isValid(Claims claims) {
        return claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId) &&
                claims.get(NONCE_KEY, String.class).equals(nonce);
    }

    private String encryptNonce(String value) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance(ALGORITHM);
            byte[] digest = sha256.digest(value.getBytes(UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw BusinessException.from(new InternalServerErrorCode("Apple OAuth 통신 암호화 과정 중 문제가 발생했습니다."));
        }
    }
}
