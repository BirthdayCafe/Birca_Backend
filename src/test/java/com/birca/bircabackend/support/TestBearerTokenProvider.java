package com.birca.bircabackend.support;

import com.birca.bircabackend.command.auth.token.JwtTokenProvider;
import com.birca.bircabackend.command.auth.token.TokenPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestBearerTokenProvider {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String getToken(Long id) {
        return "Bearer " + jwtTokenProvider.createToken(new TokenPayload(id));
    }
}
