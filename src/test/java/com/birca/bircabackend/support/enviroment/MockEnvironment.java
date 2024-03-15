package com.birca.bircabackend.support.enviroment;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProviderFactory;
import com.birca.bircabackend.common.upload.ImageUploader;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class MockEnvironment {

    private static final String URL = "https://s3.ap-northeast-2.amazonaws.com/fileupload/image/example.png";
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

    @MockBean
    private ImageUploader imageUploader;

    @BeforeEach
    void mockOAuthProvider() {
        given(providerFactory.getProvider(any()))
                .willReturn(MOCK_PROVIDER);
    }

    @BeforeEach
    void mockImageUploader() {
        given(imageUploader.upload(any()))
                .willReturn(URL);
    }
}
