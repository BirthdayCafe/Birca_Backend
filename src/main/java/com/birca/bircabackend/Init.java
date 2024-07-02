package com.birca.bircabackend;

import com.birca.bircabackend.command.auth.application.token.JwtTokenProvider;
import com.birca.bircabackend.command.auth.application.token.TokenPayload;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Init {

    private final JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    public void create() {
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        System.out.println("token = " + token);
    }
}
