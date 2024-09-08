package com.birca.bircabackend.command.auth.infrastructure.apple;

import java.util.List;

public record AppleKeyResponse(List<ApplePubKey> keys) {
}
