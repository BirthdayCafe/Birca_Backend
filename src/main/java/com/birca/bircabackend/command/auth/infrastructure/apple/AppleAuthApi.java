package com.birca.bircabackend.command.auth.infrastructure.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleAuth", url = "${oauth.apple.url}")
public interface AppleAuthApi {

    @GetMapping("/keys")
    ResponseEntity<AppleKeyResponse> getKey();
}
