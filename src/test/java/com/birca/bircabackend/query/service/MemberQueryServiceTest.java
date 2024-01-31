package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.NicknameCheckResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/member-fixture.sql")
class MemberQueryServiceTest extends ServiceTest {

    @Autowired
    private MemberQueryService memberQueryService;

    @ParameterizedTest
    @CsvSource({"새 닉네임, false", "더즈, true", "더즈1, false"})
    void 중복된_닉네임인지_검사한다(String requestNickname, boolean isDuplicated) {
        // when
        NicknameCheckResponse response = memberQueryService.checkNickname(requestNickname);

        // then
        assertThat(response.success()).isEqualTo(isDuplicated);
    }
}
