package com.birca.bircabackend.command.auth.infrastructure.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoAuth", url = "https://kapi.kakao.com")
public interface KakaoAuthApi {

    @GetMapping("/v2/user/me")
    ResponseEntity<KakaoUserResponse> getUserInfo(@RequestHeader("Authorization") String accessToken);
}
