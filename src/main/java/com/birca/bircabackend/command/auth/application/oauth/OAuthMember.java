package com.birca.bircabackend.command.auth.application.oauth;

import com.birca.bircabackend.command.member.domain.Member;

public record OAuthMember(String email, String registrationId) {

    public Member toMember() {
        return Member.join(email, registrationId);
    }
}
