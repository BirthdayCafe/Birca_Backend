package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.command.auth.application.token.JwtParseUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import io.jsonwebtoken.Claims;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

public class AppleIdentityToken {

    private final String value;
    private final Map<String, String> headers;

    public AppleIdentityToken(String value) {
        this.value = value;
        this.headers = JwtParseUtil.parseHeader(value);
    }

    public Claims getClaims(List<ApplePubKey> keys) {
        PublicKey publicKey = findPublicKey(keys);
        return JwtParseUtil.parseClaims(value, publicKey)
                .getBody();
    }

    private PublicKey findPublicKey(List<ApplePubKey> keys) {
        String kid = headers.get("kid");
        String alg = headers.get("alg");
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findFirst()
                .orElseThrow(() -> BusinessException.from(new InternalServerErrorCode("apple login에 필요한 key를 찾지 못했습니다.")))
                .genaratePublicKey();
    }
}
