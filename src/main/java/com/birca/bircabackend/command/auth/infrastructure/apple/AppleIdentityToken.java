package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.token.JwtParseUtil;
import com.birca.bircabackend.command.auth.infrastructure.OAuthConst;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import io.jsonwebtoken.Claims;
import lombok.Getter;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

@Getter
public class AppleIdentityToken {

    private final String value;
    private final Map<String, String> headers;
    private final Claims claims;

    public static AppleIdentityToken of(String value, List<ApplePubKey> keys) {
        return new AppleIdentityToken(value, keys);
    }

    private AppleIdentityToken(String value, List<ApplePubKey> keys) {
        this.value = value;
        this.headers = JwtParseUtil.parseHeader(value);
        PublicKey key = findPublicKey(keys);
        this.claims = JwtParseUtil.parseClaims(value, key)
                .getBody();
    }

    private PublicKey findPublicKey(List<ApplePubKey> keys) {
        return keys.stream()
                .filter(key -> key.kid().equals(getKid()) && key.alg().equals(getAlg()))
                .findFirst()
                .orElseThrow(() -> BusinessException.from(new InternalServerErrorCode("apple login에 필요한 key를 찾지 못했습니다.")))
                .genaratePublicKey();
    }

    private String getKid() {
        return headers.get("kid");
    }

    private String getAlg() {
        return headers.get("alg");
    }

    public OAuthMember toOAuthMember(AppleClaimValidator validator) {
        if (!validator.isValid(claims)) {
            throw BusinessException.from(new InternalServerErrorCode("Claims이 올바르지 않습니다."));
        }
        return new OAuthMember(
                claims.getSubject(),
                (String) claims.get("email"),
                OAuthConst.APPLE.getName()
        );
    }
}
