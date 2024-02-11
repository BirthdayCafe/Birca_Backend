package com.birca.bircabackend.command.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class IdentityKey {

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false)
    private String socialProvider;

    public static IdentityKey of(String socialId, String provider) {
        return new IdentityKey(socialId, provider);
    }
}
