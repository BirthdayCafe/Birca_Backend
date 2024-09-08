package com.birca.bircabackend.command.auth.infrastructure.kakao;

public record KakaoUserResponse(
        String id,
        KakaoAccount kakao_account
) {
    public record KakaoAccount(String email) {
    }
}
