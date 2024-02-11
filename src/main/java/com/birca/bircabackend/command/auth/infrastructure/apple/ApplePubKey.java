package com.birca.bircabackend.command.auth.infrastructure.apple;

public record ApplePubKey(
        String kty,
        String kid,
        String use,
        String alg,
        String n,
        String e
) {
}
