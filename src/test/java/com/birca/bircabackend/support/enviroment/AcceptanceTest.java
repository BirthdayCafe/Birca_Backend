package com.birca.bircabackend.support.enviroment;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProviderFactory;
import com.birca.bircabackend.support.TestBearerTokenProvider;
import com.birca.bircabackend.support.isolation.DatabaseIsolation;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DatabaseIsolation
public class AcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    protected TestBearerTokenProvider bearerTokenProvider;

    @MockBean
    private OAuthProviderFactory providerFactory;

    @BeforeEach
    void mockOAuthProvider() {
        given(providerFactory.getProvider(any()))
                .willReturn(new OAuthProvider() {
                    @Override
                    public OAuthMember getOAuthMember(String accessToken) {
                        return new OAuthMember("ldk@naver.com");
                    }

                    @Override
                    public String getProvider() {
                        return "kakao";
                    }
                });
    }
}
