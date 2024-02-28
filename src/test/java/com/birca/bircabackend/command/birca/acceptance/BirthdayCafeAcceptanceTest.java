package com.birca.bircabackend.command.birca.acceptance;

import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import com.birca.bircabackend.support.enviroment.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/birthday-cafe-fixture.sql")
public class BirthdayCafeAcceptanceTest extends AcceptanceTest {

    private static final Long MEMBER_ID = 1L;
    private static final ApplyRentalRequest REQUEST = new ApplyRentalRequest(
            1L,
            1L,
            LocalDateTime.of(2024, 2, 8, 0, 0, 0),
            LocalDateTime.of(2024, 2, 10, 0, 0, 0),
            5,
            10,
            "@ChaseM",
            "010-0000-0000"
    );

    @Test
    void 생일_카페_대관을_신청한다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .body(REQUEST)
                .post("/api/v1/birthday-cafes")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 생일_카페_대관을_취소한다() {
        // given
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .body(REQUEST)
                .post("/api/v1/birthday-cafes")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .post("/api/v1/birthday-cafes/{birthdayCafeId}/cancel", 1)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
