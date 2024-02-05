package com.birca.bircabackend.command.cafe.acceptance;

import com.birca.bircabackend.command.cafe.application.BusinessLicenseFacade;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseOcrService;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseService;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseVerificationService;
import com.birca.bircabackend.command.cafe.presentation.BusinessLicenseController;
import com.birca.bircabackend.support.enviroment.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/member-fixture.sql")
public class BusinessLicenseAcceptanceTest extends AcceptanceTest {

    @InjectMocks
    private BusinessLicenseController businessLicenseController;

    @MockBean
    private BusinessLicenseVerificationService businessLicenseVerificationService;

    @MockBean
    private BusinessLicenseOcrService businessLicenseOcrService;

    @MockBean
    private BusinessLicenseService businessLicenseService;

    @MockBean
    private BusinessLicenseFacade businessLicenseFacade;

    private static final Long MEMBER_ID = 1L;

    @Test
    void 사업자등록증을_스캔한다() {
        // given
        File businessLicense = new File("src/test/resources/businessLicense.jpg");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .multiPart("businessLicense", businessLicense)
                .post("/api/v1/cafes/license-read")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 사업자등록증을_저장한다() {
        // given
        File businessLicense = new File("src/test/resources/businessLicense.jpg");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .multiPart("businessLicense", businessLicense)
                .param("cafeName", "커피 벌스데이")
                .param("businessLicenseNumber", "128-39-49844")
                .param("owner", "최민혁")
                .param("address", "서울 마포구 와우산로29길 26-33 1층 커피 벌스데이")
                .post("/api/v1/cafes/apply")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
