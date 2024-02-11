package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplePubKeys {

    private final List<ApplePubKey> values;

    public static ApplePubKeys from(List<ApplePubKey> values) {
        return new ApplePubKeys(new ArrayList<>(values));
    }

    public PublicKey findPublicKeyOf(String kid, String alg) {
        return values.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findFirst()
                .orElseThrow(() -> BusinessException.from(new InternalServerErrorCode("apple login에 필요한 key를 찾지 못했습니다.")))
                .genaratePublicKey();
    }
}
