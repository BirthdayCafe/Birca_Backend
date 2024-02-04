package com.birca.bircabackend.command.auth.infrastructure.kakao;

public record KakaoUserResponse(KakaoAccount kakaoAccount) {
    public record KakaoAccount(String email) {
    }
}
