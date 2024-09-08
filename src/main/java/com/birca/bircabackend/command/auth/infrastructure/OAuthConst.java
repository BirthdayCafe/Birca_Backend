package com.birca.bircabackend.command.auth.infrastructure;

import lombok.Getter;

@Getter
public enum OAuthConst {
    KAKAO("kakao"), APPLE("apple");

    private final String name;

    OAuthConst(String name) {
        this.name = name;
    }
}
