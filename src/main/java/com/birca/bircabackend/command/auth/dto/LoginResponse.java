package com.birca.bircabackend.command.auth.dto;

public record LoginResponse(
        String accessToken,
        Boolean isNewMember,
        String lastRole
) {
}
