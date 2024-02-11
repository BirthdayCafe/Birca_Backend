package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.application.token.JwtParser;
import com.birca.bircabackend.common.ApiResponseExtractor;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class AppleOAuthProvider implements OAuthProvider {

    private static final String PROVIDER_NAME = "apple";
    private static final String KID = "kid";
    private static final String ALG = "alg";
    private static final String ISS = "https://appleid.apple.com/auth";

    @Value("${oauth.apple.client-id:defaultId}")
    private String clientId;
    @Value("${oauth.apple.nonce:defaultNonce}")
    private String nonce;

    private final AppleAuthApi appleAuthApi;
    private final JwtParser jwtParser;

    @Override
    public OAuthMember getOAuthMember(String accessToken) {
        Map<String, String> header = jwtParser.parseHeader(accessToken);

        AppleKeyResponse keyResponse = ApiResponseExtractor.getBody(appleAuthApi.getKey());
        ApplePubKeys keys = ApplePubKeys.from(keyResponse.keys());
        ApplePubKey key = keys.findKeyOf(header.get(KID), header.get(ALG));

        PublicKey publicKey = key.genaratePublicKey();
        Claims claims = jwtParser.parseClaims(accessToken, publicKey).getBody();
        validateClaims(claims);
        return new OAuthMember(
                claims.getSubject(),
                (String) claims.get("email"),
                PROVIDER_NAME
        );
    }

    private void validateClaims(Claims claims) {
        boolean isValid = claims.getIssuer().contains(ISS) &&
                claims.getAudience().equals(clientId) &&
                claims.get(encryptNonce(nonce), String.class).equals(nonce);
        if (!isValid) {
            throw BusinessException.from(new InternalServerErrorCode("Claims이 올바르지 않습니다."));
        }
    }

    private String encryptNonce(String value) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
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

    @Override
    public String getProvider() {
        return PROVIDER_NAME;
    }
}
