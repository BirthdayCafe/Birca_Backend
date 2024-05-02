package com.birca.bircabackend.command.cafe.acceptance;

import com.birca.bircabackend.command.cafe.dto.CafeUpdateRequest;
import com.birca.bircabackend.command.cafe.dto.DayOffCreateRequest;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/cafe-fixture.sql")
public class CafeAcceptanceTest extends AcceptanceTest {

    @Test
    void 카페_상세_정보를_수정한다() {
        //given
        CafeUpdateRequest request = new CafeUpdateRequest(
                "메가커피",
                "서울특별시 강남구 테헤란로 212",
                "@ChaseM",
                "8시 - 22시",
                List.of(new CafeUpdateRequest.CafeMenuResponse("아메리카노", 1500)),
                List.of(new CafeUpdateRequest.CafeOptionResponse("액자", 2000))
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L))
                .body(request)
                .patch("/api/v1/cafes")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 카페_휴무일을_지정한다() {
        // given
        DayOffCreateRequest request = new DayOffCreateRequest(
                List.of(
                        LocalDateTime.of(2024, 5, 5, 0, 0, 0),
                        LocalDateTime.of(2024, 5, 15, 0, 0, 0)
                )
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L))
                .body(request)
                .post("/api/v1/cafes/{cafeId}/day-off", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
