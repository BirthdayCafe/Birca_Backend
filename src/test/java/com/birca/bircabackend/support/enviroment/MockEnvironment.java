package com.birca.bircabackend.support.enviroment;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class MockEnvironment {

    private static final String URL ="https://s3.ap-northeast-2.amazonaws.com/fileupload/image/example.png";
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

    @Autowired
    private AmazonS3 amazonS3;

    @BeforeEach
    void mockOAuthProvider() {
        given(providerFactory.getProvider(any()))
                .willReturn(MOCK_PROVIDER);
    }

    @BeforeEach
    void mockAwsS3() throws MalformedURLException {
        given(amazonS3.putObject(any(PutObjectRequest.class)))
                .willReturn(new PutObjectResult());
        given(amazonS3.getUrl(any(), any()))
                .willReturn(new URL(URL));
    }
}
