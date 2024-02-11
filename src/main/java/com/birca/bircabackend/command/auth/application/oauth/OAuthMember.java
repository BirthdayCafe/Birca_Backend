package com.birca.bircabackend.command.auth.application.oauth;

public record OAuthMember(
        String id,
        String email,
        String provider) {
}
