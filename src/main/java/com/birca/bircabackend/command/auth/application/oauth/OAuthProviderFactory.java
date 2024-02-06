package com.birca.bircabackend.command.auth.application.oauth;

import com.birca.bircabackend.common.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.birca.bircabackend.command.auth.exception.AuthErrorCode.NOT_EXISTS_PROVIDER;
import static java.util.stream.Collectors.toMap;

@Component
public class OAuthProviderFactory {

    private final Map<String, OAuthProvider> oAuthProviders;

    public OAuthProviderFactory(List<OAuthProvider> oAuthProviders) {
        this.oAuthProviders = oAuthProviders.stream()
                .collect(toMap(OAuthProvider::getProvider, provider -> provider));
    }

    public OAuthProvider getProvider(String provider) {
        return Optional.ofNullable(oAuthProviders.get(provider))
                .orElseThrow(() -> BusinessException.from(NOT_EXISTS_PROVIDER));
    }
}
