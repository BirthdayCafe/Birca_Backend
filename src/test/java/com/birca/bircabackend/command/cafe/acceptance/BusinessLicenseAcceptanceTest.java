package com.birca.bircabackend.command.cafe.acceptance;

import com.birca.bircabackend.support.enviroment.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/member-fixture.sql")
public class BusinessLicenseAcceptanceTest extends AcceptanceTest {

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
                .param("cafeName", "cafeName")
                .param("businessLicenseNumber", "128-39-49844")
                .param("owner", "owner")
                .param("address", "address")
                .post("/api/v1/cafes/apply")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 국세청에_등록되지_않은_사업자등록번호은_예외가_발생한다() {
        // given
        File businessLicense = new File("src/test/resources/businessLicense.jpg");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .multiPart("businessLicense", businessLicense)
                .param("cafeName", "cafeName")
                .param("businessLicenseNumber", "000-00-0000")
                .param("owner", "owner")
                .param("address", "address")
                .post("/api/v1/cafes/apply")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
