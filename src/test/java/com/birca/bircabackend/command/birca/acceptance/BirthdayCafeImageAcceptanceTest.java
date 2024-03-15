package com.birca.bircabackend.command.birca.acceptance;

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

@Sql("/fixture/birthday-cafe-image-fixture.sql")
public class BirthdayCafeImageAcceptanceTest extends AcceptanceTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long BIRTHDAY_CAFE_ID = 1L;

    @Test
    void 생일_카페_이미지를_저장한다() {
        //given
        File birthdayCafeImage = new File("src/test/resources/birthdayCafe.jpeg");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .multiPart("images", birthdayCafeImage)
                .post("/api/v1/birthday-cafes/{birthdayCafeId}/images", BIRTHDAY_CAFE_ID)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
