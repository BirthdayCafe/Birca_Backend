package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.BusinessLicense;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/member-fixture.sql")
class BusinessLicenseServiceTest extends ServiceTest {

    @Autowired
    private BusinessLicenseService businessLicenseService;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("사업자등록증 저장 시")
    class saveBusinessLicenseTest {

        private static final Long OWNER_ID = 1L;
        private static final String IMAGE_URL = "image.com";
        private static final MockMultipartFile BUSINESS_LICENSE =
                new MockMultipartFile("businessLicense", "businessLicense.pdf",
                        "application/pdf", "businessLicense".getBytes(UTF_8)
                );

        @Test
        void 정상적으로_저장한다() {
            // given
            BusinessLicenseCreateRequest request = createBusinessLicenseCreateRequest("123-45-67890");

            // when
            businessLicenseService.saveBusinessLicense(OWNER_ID, request, IMAGE_URL);
            EntityUtil entityUtil = new EntityUtil(entityManager);
            BusinessLicense findBusinessLicense = entityUtil
                    .getEntity(BusinessLicense.class, 1L, BUSINESS_LICENSE_NOT_FOUND);

            // then
            assertThat(findBusinessLicense.getCafeName()).isEqualTo("카페 벌스데이");
            assertThat(findBusinessLicense.getOwnerName()).isEqualTo("최민혁");
            assertThat(findBusinessLicense.getAddress()).isEqualTo("서울 마포구 와우산로29길 26-33 1층 커피 벌스데이");
            assertThat(findBusinessLicense.getCode().getTaxOfficeCode()).isEqualTo("123");
            assertThat(findBusinessLicense.getCode().getBusinessTypeCode()).isEqualTo("45");
            assertThat(findBusinessLicense.getCode().getSerialCode()).isEqualTo("67890");
        }

        @Test
        void 이미_등록된_사업자등록증이면_예외가_발생한다() {
            // given
            BusinessLicenseCreateRequest request = createBusinessLicenseCreateRequest("123-45-67890");
            businessLicenseService.saveBusinessLicense(OWNER_ID, request, IMAGE_URL);

            // when then
            assertThatThrownBy(() -> businessLicenseService.saveBusinessLicense(OWNER_ID, request, IMAGE_URL))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(DUPLICATE_BUSINESS_LICENSE_NUMBER);
        }

        @ParameterizedTest
        @ValueSource(strings = {"0123456789", "01-12-34567", "012-3-45678", "012-23-4567"})
        void 사업자등록번호가_형식에_맞지_않으면_예외가_발생한다(String businessLicenseNumber) {
            // given
            BusinessLicenseCreateRequest request = createBusinessLicenseCreateRequest(businessLicenseNumber);

            // when then
            assertThatThrownBy(() -> businessLicenseService.saveBusinessLicense(OWNER_ID, request, IMAGE_URL))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(INVALID_BUSINESS_LICENSE_NUMBER);
        }

        private BusinessLicenseCreateRequest createBusinessLicenseCreateRequest(String businessLicenseNumber) {
            return new BusinessLicenseCreateRequest(
                    BUSINESS_LICENSE, "카페 벌스데이", businessLicenseNumber,
                    "최민혁", "서울 마포구 와우산로29길 26-33 1층 커피 벌스데이"
            );
        }
    }
}
