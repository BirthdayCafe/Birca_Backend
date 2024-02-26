package com.birca.bircabackend.query.service;


import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/favorite-artist-fixture.sql")
class FavoriteArtistQueryServiceTest extends ServiceTest {

    @Autowired
    private FavoriteArtistQueryService favoriteArtistQueryService;

    @Test
    void 최애_아티스트를_조회한다() {
        // given
        LoginMember loginMember = new LoginMember(1L);

        // when
        ArtistResponse actual = favoriteArtistQueryService.findFavoriteArtist(loginMember);

        // then
        assertThat(actual).isEqualTo(new ArtistResponse(10L, "민지", "image10.com"));
    }

    @Test
    void 최애_아티스트가_없는_경우를_조회한다() {
        // given
        LoginMember loginMember = new LoginMember(2L);

        // when
        ArtistResponse actual = favoriteArtistQueryService.findFavoriteArtist(loginMember);

        // then
        assertThat(actual).isEqualTo(new ArtistResponse(null, null, null));
    }

    @Test
    void 존재하지_않은_회원이_관심_아티스트_목록을_조회하면_예외가_발생한다() {
        // when then
        assertThatThrownBy(() -> favoriteArtistQueryService.findFavoriteArtist(new LoginMember(100L)))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}
