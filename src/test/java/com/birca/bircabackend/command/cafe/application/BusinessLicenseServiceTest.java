package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql("/fixture/member-fixture.sql")
class BusinessLicenseServiceTest extends ServiceTest {

    @Autowired
    private BusinessLicenseService businessLicenseService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void 사업자등록증을_저장한다() {
        // given
        MockMultipartFile businessLicense = createMockMultipartFile();
        BusinessLicenseCreateRequest request = createBusinessLicenseCreateRequest(businessLicense,
                "123-45-67890");

        // when
        businessLicenseService.saveBusinessLicense(new LoginMember(1L), request);
        EntityUtil entityUtil = new EntityUtil(entityManager);
        BusinessLicense findBusinessLicense = entityUtil
                .getEntity(BusinessLicense.class, 1L, BUSINESS_LICENSE_NOT_FOUND);

        // then
        assertThat(findBusinessLicense.getCafeName()).isEqualTo("카페 벌스데이");
        assertThat(findBusinessLicense.getOwnerName()).isEqualTo("최민혁");
        assertThat(findBusinessLicense.getAddress()).isEqualTo("서울 마포구 와우산로29길 26-33 1층 커피 벌스데이");
        assertThat(findBusinessLicense.getCode().getTaxOfficeCode()).isEqualTo(123);
        assertThat(findBusinessLicense.getCode().getBusinessTypeCode()).isEqualTo(45);
        assertThat(findBusinessLicense.getCode().getSerialCode()).isEqualTo(67890);
    }

    @Test
    void 이미_등록된_사업자등록증이면_예외가_발생한다() {
        // given
        LoginMember loginMember = new LoginMember(1L);
        MockMultipartFile businessLicense = createMockMultipartFile();
        BusinessLicenseCreateRequest request = createBusinessLicenseCreateRequest(businessLicense,
                "123-45-67890");
        businessLicenseService.saveBusinessLicense(loginMember, request);

        // when then
        assertThatThrownBy(() -> businessLicenseService.saveBusinessLicense(loginMember, request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(DUPLICATE_BUSINESS_LICENSE_NUMBER);
    }

    @Nested
    @DisplayName("사업자등록증 번호의")
    class validateBusinessLicenseNumberTest {

        @Test
        void 세무서_번호_길이가_3이_아니면_예외가_발생한다() {
            // given
            MockMultipartFile businessLicense = createMockMultipartFile();
            BusinessLicenseCreateRequest request = createBusinessLicenseCreateRequest(businessLicense,
                    "13-45-67890");

            // when then
            assertThatThrownBy(() -> businessLicenseService.saveBusinessLicense(new LoginMember(1L), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(INVALID_TAX_OFFICE_CODE_LENGTH);
        }

        @Test
        void 사업자_성격_번호_길이가_2가_아니면_예외가_발생한다() {
            // given
            MockMultipartFile businessLicense = createMockMultipartFile();
            BusinessLicenseCreateRequest request = createBusinessLicenseCreateRequest(businessLicense,
                    "123-4-67890");

            // when then
            assertThatThrownBy(() -> businessLicenseService.saveBusinessLicense(new LoginMember(1L), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(INVALID_BUSINESS_TYPE_CODE_LENGTH);
        }

        @Test
        void 일련_번호_길이가_5가_아니면_예외가_발생한다() {
            // given
            MockMultipartFile businessLicense = createMockMultipartFile();
            BusinessLicenseCreateRequest request = createBusinessLicenseCreateRequest(businessLicense,
                    "123-45-6");

            // when then
            assertThatThrownBy(() -> businessLicenseService.saveBusinessLicense(new LoginMember(1L), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(INVALID_SERIAL_CODE_LENGTH);
        }
    }

    private MockMultipartFile createMockMultipartFile() {
        return new MockMultipartFile("businessLicense",
                "businessLicense.pdf", "application/pdf", "businessLicense".getBytes(UTF_8));
    }

    private BusinessLicenseCreateRequest createBusinessLicenseCreateRequest(MockMultipartFile businessLicense,
                                                                            String businessLicenseNumber) {
        return new BusinessLicenseCreateRequest(
                businessLicense, "카페 벌스데이", businessLicenseNumber,
                "최민혁", "서울 마포구 와우산로29길 26-33 1층 커피 벌스데이"
        );
    }
}