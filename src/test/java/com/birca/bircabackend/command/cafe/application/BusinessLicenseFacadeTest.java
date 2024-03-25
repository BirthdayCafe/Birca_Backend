package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Sql("/fixture/business-license-fixture.sql")
class BusinessLicenseFacadeTest extends ServiceTest {

    @Autowired
    private BusinessLicenseFacade businessLicenseFacade;

    @Nested
    @DisplayName("사업자등록증을")
    class BusinessLicenseTest {

        private static final LoginMember LOGIN_MEMBER = new LoginMember(1L);
        private static final MockMultipartFile BUSINESS_LICENSE = new MockMultipartFile(
                "businessLicense", "businessLicense".getBytes(UTF_8)
        );

        @Test
        void 스캔하고_업로드_횟수를_증가시킨다() {
            // when
            BusinessLicenseResponse actual = businessLicenseFacade.getBusinessLicenseInfoAndUploadCount(LOGIN_MEMBER, BUSINESS_LICENSE);

            // then
            verify(ocrProvider, times(1)).getBusinessLicenseInfo(any());
            assertThat(actual).isEqualTo(new BusinessLicenseResponse("STARBUCKS", "123-45-67890", "최민혁", "서울 중앙로 212 빌딩 1층", 1));
        }

        @Test
        void 저장한다() {
            // given
            BusinessLicenseCreateRequest request =
                    new BusinessLicenseCreateRequest(
                            BUSINESS_LICENSE, "STARBUCKS", "123-45-67890", "최민혁", "서울 중앙로 212 빌딩 1층"
                    );

            // when
            businessLicenseFacade.saveBusinessLicense(LOGIN_MEMBER, request);

            // then
            verify(imageUploader, times(1)).upload(any());
        }
    }
}
