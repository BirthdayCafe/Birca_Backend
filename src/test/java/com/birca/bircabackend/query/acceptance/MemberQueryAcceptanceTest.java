package com.birca.bircabackend.query.acceptance;

import com.birca.bircabackend.support.enviroment.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@Sql("/fixture/member-fixture.sql")
class MemberQueryAcceptanceTest extends AcceptanceTest {

    @ParameterizedTest
    @CsvSource({"새 닉네임, false", "더즈, true"})
    void 중복된_닉네임인지_검사한다(String requestNickname, boolean isDuplicated) {
        // given
        Long memberId = 1L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(memberId))
                .param("nickname", requestNickname)
                .get("/api/v1/join/check-nickname")
                .then().log().all()
                .extract();

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getBoolean("success")).isEqualTo(isDuplicated)
        );
    }
}
