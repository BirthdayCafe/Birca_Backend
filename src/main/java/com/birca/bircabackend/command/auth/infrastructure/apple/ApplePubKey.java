package com.birca.bircabackend.command.auth.infrastructure.apple;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public record ApplePubKey(
        String kty,
        String kid,
        String use,
        String alg,
        String n,
        String e
) {

    private static final int POSITIVE_SIGNUM = 1;
    private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();

    public PublicKey genaratePublicKey() {
        BigInteger integerN = new BigInteger(POSITIVE_SIGNUM, BASE64_URL_DECODER.decode(n));
        BigInteger integerE = new BigInteger(POSITIVE_SIGNUM, BASE64_URL_DECODER.decode(e));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(kty);
            return keyFactory.generatePublic(new RSAPublicKeySpec(integerN, integerE));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException exception) {
            throw BusinessException.from(new InternalServerErrorCode(exception.getMessage()));
        }
    }
}
