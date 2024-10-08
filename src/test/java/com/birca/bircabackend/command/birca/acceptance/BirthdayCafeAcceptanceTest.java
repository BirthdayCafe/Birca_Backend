package com.birca.bircabackend.command.birca.acceptance;

import com.birca.bircabackend.command.birca.dto.*;
import com.birca.bircabackend.support.enviroment.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.com.github.dockerjava.core.dockerfile.DockerfileStatement;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/birthday-cafe-fixture.sql")
public class BirthdayCafeAcceptanceTest extends AcceptanceTest {

    private static final Long HOST_1_ID = 1L;
    private static final Long HOST_2_ID = 2L;
    private static final Long RENTAL_PENDING_CAFE = 1L;
    private static final Long IN_PROGRESS_CAFE = 4L;
    private static final ApplyRentalRequest APPLY_RENTAL_REQUEST = new ApplyRentalRequest(
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
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(HOST_2_ID))
                .body(APPLY_RENTAL_REQUEST)
                .post("/api/v1/birthday-cafes")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 생일_카페_대관을_취소한다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(HOST_1_ID))
                .post("/api/v1/birthday-cafes/{birthdayCafeId}/cancel", RENTAL_PENDING_CAFE)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 생일_카페_특전_상태를_변경한다() {
        StateChangeRequest request = new StateChangeRequest("SCARCE");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(HOST_1_ID))
                .body(request)
                .patch("/api/v1/birthday-cafes/{birthdayCafeId}/specialGoods", IN_PROGRESS_CAFE)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 생일_카페_혼잡도_상태를_변경한다() {
        // given
        StateChangeRequest request = new StateChangeRequest("MODERATE");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(HOST_1_ID))
                .body(request)
                .patch("/api/v1/birthday-cafes/{birthdayCafeId}/congestion", IN_PROGRESS_CAFE)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 생일_카페_공개_상태를_변경한다() {
        // given
        StateChangeRequest request = new StateChangeRequest("PUBLIC");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(HOST_1_ID))
                .body(request)
                .patch("/api/v1/birthday-cafes/{birthdayCafeId}/visibility", IN_PROGRESS_CAFE)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 생일_카페_특전을_추가한다() {
        // given
        List<SpecialGoodsRequest> request = List.of(
                new SpecialGoodsRequest("기본", "종이컵, 포토카드"),
                new SpecialGoodsRequest("디저트", "종이컵, 포토카드, ID카드")
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(HOST_1_ID))
                .body(request)
                .post("/api/v1/birthday-cafes/{birthdayCafeId}/special-goods", IN_PROGRESS_CAFE)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 생일_카페_메뉴를_추가한다() {
        // given
        List<MenuRequest> request = List.of(
                new MenuRequest("기본", "아메리카노+포토카드+ID카드", 10000),
                new MenuRequest("디저트", "케이크+포토카드+ID카드", 10000)
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(HOST_1_ID))
                .body(request)
                .post("/api/v1/birthday-cafes/{birthdayCafeId}/menus", IN_PROGRESS_CAFE)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 생일_카페_럭키_드로우를_추가한다() {
        // given
        List<LuckyDrawRequest> request = List.of(
                new LuckyDrawRequest(1, "머그컵"),
                new LuckyDrawRequest(2, "포토 카드")
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(HOST_1_ID))
                .body(request)
                .post("/api/v1/birthday-cafes/{birthdayCafeId}/lucky-draws", IN_PROGRESS_CAFE)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 생일_카페_정보를_수정한다() {
        // given
        BirthdayCafeUpdateRequest request = new BirthdayCafeUpdateRequest("BTS 생카", "@bts-birca");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(HOST_1_ID))
                .body(request)
                .patch("/api/v1/birthday-cafes/{birthdayCafeId}", IN_PROGRESS_CAFE)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @Sql("/fixture/birthday-cafe-application-fixture.sql")
    void 생일_카페_신청을_수락한다() {
        // given
        Long ownerId = 2L;
        Long rentalPendingBirthdayCafeId = 4L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(ownerId))
                .post("/api/v1/owners/birthday-cafes/{birthdayCafeId}/approve", rentalPendingBirthdayCafeId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @Sql("/fixture/birthday-cafe-application-fixture.sql")
    void 생일_카페_신청을_거절한다() {
        // given
        Long ownerId = 2L;
        Long rentalPendingBirthdayCafeId = 4L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(ownerId))
                .post("/api/v1/owners/birthday-cafes/{birthdayCafeId}/cancel", rentalPendingBirthdayCafeId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @Sql("/fixture/birthday-cafe-schedule-fixture.sql")
    void 카페_사장이_생일_카페_일정을_추가한다() {
        // given
        Long ownerId = 3L;
        AddBirthdayCafeSchedule request = new AddBirthdayCafeSchedule(
                1L,
                LocalDateTime.of(2024, 7, 8, 0, 0, 0),
                LocalDateTime.of(2024, 7, 10, 0, 0, 0),
                5,
                10,
                "@ChaseM",
                "010-0000-0000"
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(ownerId))
                .body(request)
                .post("/api/v1/owners/birthday-cafes/schedules")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
