package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptUtil {

    private static final String ALGORITHM = "SHA-256";

    public static String encrypt(String value) {
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
