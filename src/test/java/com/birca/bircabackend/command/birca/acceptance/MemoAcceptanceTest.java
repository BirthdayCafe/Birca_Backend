package com.birca.bircabackend.command.birca.acceptance;

import com.birca.bircabackend.command.birca.dto.MemoRequest;
import com.birca.bircabackend.support.enviroment.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/memo-fixture.sql")
public class MemoAcceptanceTest extends AcceptanceTest {

    @Test
    void 생일_카페_내용을_메모한다() {
        // given
        Long birthdayCafeId = 1L;
        Long memberId = 3L;
        MemoRequest request = new MemoRequest("생일 카페 메모 내용");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(memberId))
                .body(request)
                .post("/api/v1/{birthdayCafeId}/memo", birthdayCafeId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
