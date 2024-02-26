package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.artist.exception.ArtistErrorCode;
import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/interest-artist-fixture.sql")
class InterestArtistQueryServiceTest extends ServiceTest {

    @Autowired
    private InterestArtistQueryService interestArtistQueryService;

    @Test
    void 관심_아티스트_목록을_조회한다() {
        // when
        List<ArtistResponse> actual = interestArtistQueryService.findInterestArtists(new LoginMember(1L));

        // then
        assertThat(actual)
                .containsOnly(
                        new ArtistResponse(8L, "하니", "image8.com"),
                        new ArtistResponse(9L, "해린", "image9.com"),
                        new ArtistResponse(10L, "민지", "image10.com"),
                        new ArtistResponse(11L, "다니엘", "image11.com"),
                        new ArtistResponse(12L, "혜인", "image12.com")
                );
    }

    @Test
    void 존재하지_않은_회원이_관심_아티스트_목록을_조회하면_예외가_발생한다() {
        // when then
        assertThatThrownBy(() -> interestArtistQueryService.findInterestArtists(new LoginMember(100L)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}
