package com.birca.bircabackend.command.auth.application.oauth;

public interface OAuthProvider {

    OAuthMember getOAuthMember(String accessToken);

    String getProvider();
}
