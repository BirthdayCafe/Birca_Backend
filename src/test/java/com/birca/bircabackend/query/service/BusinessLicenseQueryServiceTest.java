package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.BusinessLicenseStatusResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/business-license-fixture.sql")
class BusinessLicenseQueryServiceTest extends ServiceTest {

    @Autowired
    private BusinessLicenseQueryService businessLicenseQueryService;

    @ParameterizedTest
    @CsvSource({"100, true", "1001, false"})
    void 사업자등록증_승인_여부를_조회한다(Long ownerId, Boolean registrationApproved) {
        // given
        LoginMember loginMember = new LoginMember(ownerId);

        // when
        BusinessLicenseStatusResponse actual = businessLicenseQueryService.getBusinessLicenseStatus(loginMember);

        // then
        assertThat(actual.registrationApproved()).isEqualTo(registrationApproved);
    }
}