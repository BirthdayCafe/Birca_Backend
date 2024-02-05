package com.birca.bircabackend.command.auth.infrastructure.kakao;

public record KakaoUserResponse(KakaoAccount kakao_account) {
    public record KakaoAccount(String email) {
    }
}
