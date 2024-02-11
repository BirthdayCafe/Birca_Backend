package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.command.auth.application.token.JwtParser;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleClaimsParser {

    private final JwtParser jwtParser;

    public Claims parseClaims(String accessToken, List<ApplePubKey> keys) {
        Map<String, String> header = jwtParser.parseHeader(accessToken);
        PublicKey publicKey = findPublicKeyOf(keys, header.get("kid"), header.get("alg"));
        return jwtParser.parseClaims(accessToken, publicKey)
                .getBody();
    }

    private PublicKey findPublicKeyOf(List<ApplePubKey> keys, String kid, String alg) {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findFirst()
                .orElseThrow(() -> BusinessException.from(new InternalServerErrorCode("apple login에 필요한 key를 찾지 못했습니다.")))
                .genaratePublicKey();
    }
}
