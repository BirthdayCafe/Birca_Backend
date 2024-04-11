package com.birca.bircabackend.command.cafe.acceptance;

import com.birca.bircabackend.command.cafe.dto.CafeImageDeleteRequest;
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

@Sql("/fixture/cafe-image-fixture.sql")
public class CafeImageAcceptanceTest extends AcceptanceTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 카페_이미지를_저장한다() {
        // given
        Long cafeId = 2L;
        File cafeImage = new File("src/test/resources/cafe.jpeg");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .multiPart("cafeImage", cafeImage)
                .post("/api/v1/cafes/{cafeId}/images", cafeId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 카페_이미지를_삭제한다() {
        // given
        Long cafeId = 1L;
        CafeImageDeleteRequest request = new CafeImageDeleteRequest("image1.com");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .body(request)
                .delete("/api/v1/cafes/{cafeId}/images", cafeId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
