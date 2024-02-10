package com.birca.bircabackend.support.enviroment;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class MockOAuthEnvironment {

    private static final OAuthProvider MOCK_PROVIDER = new OAuthProvider() {
        @Override
        public OAuthMember getOAuthMember(String accessToken) {
            return new OAuthMember("23243", "ldk@naver.com", "kakao");
        }

        @Override
        public String getProvider() {
            return "kakao";
        }
    };

    @MockBean
    private OAuthProviderFactory providerFactory;

    @BeforeEach
    void mockOAuthProvider() {
        given(providerFactory.getProvider(any()))
                .willReturn(MOCK_PROVIDER);
    }
}
