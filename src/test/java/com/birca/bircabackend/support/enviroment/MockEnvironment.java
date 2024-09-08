package com.birca.bircabackend.support.enviroment;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProvider;
import com.birca.bircabackend.command.auth.application.oauth.OAuthProviderFactory;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseStatusVerifier;
import com.birca.bircabackend.command.cafe.application.OcrProvider;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseInfoResponse;
import com.birca.bircabackend.common.image.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

public class MockEnvironment {

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
    private static final String URL = "https://s3.ap-northeast-2.amazonaws.com/fileupload/image/example.png";
    private static final BusinessLicenseInfoResponse RESPONSE = new BusinessLicenseInfoResponse(
            "STARBUCKS", "123-45-67890",
            "최민혁", "서울 중앙로 212 빌딩 1층");

    @MockBean
    private OAuthProviderFactory providerFactory;

    @MockBean
    protected ImageRepository imageRepository;

    @MockBean
    protected OcrProvider ocrProvider;

    @MockBean
    protected BusinessLicenseStatusVerifier businessLicenseStatusVerifier;

    @BeforeEach
    void mockOAuthProvider() {
        given(providerFactory.getProvider(any()))
                .willReturn(MOCK_PROVIDER);
    }

    @BeforeEach
    void mockImageUploader() {
        given(imageRepository.upload(any()))
                .willReturn(URL);
        doNothing().when(imageRepository)
                .delete(any());
    }

    @BeforeEach
    void mockOcrProvider() {
        given(ocrProvider.getBusinessLicenseInfo(any()))
                .willReturn(RESPONSE);
    }

    @BeforeEach
    void mockBusinessLicenseVerifier() {
        doNothing().when(businessLicenseStatusVerifier)
                .verifyBusinessLicenseStatus(any());
    }
}
