package com.birca.bircabackend.query.acceptance;

import com.birca.bircabackend.support.enviroment.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ArtistQueryAcceptanceTest extends AcceptanceTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 아티스트_그룹_목록을_조회한다() {
        // given

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .get("/api/v1/artist-groups")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }
}
