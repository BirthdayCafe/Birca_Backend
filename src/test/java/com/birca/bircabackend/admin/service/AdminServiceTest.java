package com.birca.bircabackend.admin.service;

import com.birca.bircabackend.admin.dto.AdminAuthRequest;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.BUSINESS_LICENSE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/admin-fixture.sql")
class AdminServiceTest extends ServiceTest {

    @Autowired
    private AdminService adminService;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("사업자등록증 승인 시")
    class ApproveBusinessLicenseTest {

        private static final AdminAuthRequest REQUEST = new AdminAuthRequest("id", "password");

        @Test
        void 정상적으로_승인한다() {
            // given
            Long businessLicenseId = 1L;

            // when
            adminService.approveBusinessLicense(businessLicenseId, REQUEST);
            Cafe expectedCafe = entityManager.createQuery("select c from Cafe c where c.businessLicenseId = :businessLicenseId", Cafe.class)
                    .setParameter("businessLicenseId", businessLicenseId)
                    .getSingleResult();

            Boolean expectedRegistrationApproved = entityManager.createQuery("select bl.registrationApproved from BusinessLicense bl where bl.id = :businessLicenseId", Boolean.class)
                    .setParameter("businessLicenseId", businessLicenseId)
                    .getSingleResult();

            // then
            assertAll(
                    () -> assertThat(expectedCafe.getId()).isEqualTo(1L),
                    () -> assertThat(expectedCafe.getOwnerId()).isEqualTo(1L),
                    () -> assertThat(expectedCafe.getBusinessLicenseId()).isEqualTo(1L),
                    () -> assertThat(expectedCafe.getName()).isEqualTo("스타벅스"),
                    () -> assertThat(expectedCafe.getAddress()).isEqualTo("서울특별시 강남구 테헤란로 212"),

                    () -> assertThat(expectedRegistrationApproved).isTrue()
            );
        }

        @Test
        void 관리자_아이디_비밀번호가_일치하지_않으면_예외가_발생한다() {
            // given
            Long businessLicenseId = 1L;
            AdminAuthRequest request = new AdminAuthRequest("wrong-id", "wrong-password");

            // when then
            assertThatThrownBy(() -> adminService.approveBusinessLicense(businessLicenseId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .extracting("message")
                    .isEqualTo("잘못된 관리자 인증 정보입니다.");
        }

        @Test
        void 존재하지_않는_사업자등록증은_예외가_발생한다() {
            // given
            Long businessLicenseId = 100L;

            // when then
            assertThatThrownBy(() -> adminService.approveBusinessLicense(businessLicenseId, REQUEST))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BUSINESS_LICENSE_NOT_FOUND);
        }
    }
}
