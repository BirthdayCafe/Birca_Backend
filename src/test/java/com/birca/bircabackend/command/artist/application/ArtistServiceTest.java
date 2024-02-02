package com.birca.bircabackend.command.artist.application;

import com.birca.bircabackend.command.artist.domain.FavoriteArtist;
import com.birca.bircabackend.command.artist.domain.InterestArtist;
import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.command.artist.dto.InterestArtistRequest;
import com.birca.bircabackend.command.artist.exception.ArtistErrorCode;
import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/artist-fixture.sql")
class ArtistServiceTest extends ServiceTest {

    private static final long MEMBER_ID = 1L;
    private static final LoginMember LOGIN_MEMBER = new LoginMember(MEMBER_ID);

    @Autowired
    private ArtistService artistService;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("최애 아티스트를")
    class RegisterFavoriteArtistTest {

        @Test
        void 정상적으로_등록한다() {
            // given
            long favoriteArtistId = 8L;
            FavoriteArtistRequest request = new FavoriteArtistRequest(favoriteArtistId);

            // when
            artistService.registerFavoriteArtist(request, LOGIN_MEMBER);

            // then
            FavoriteArtist favoriteArtist = entityManager.find(FavoriteArtist.class, 1L);
            assertAll(
                    ()-> assertThat(favoriteArtist.getArtistId()).isEqualTo(favoriteArtistId),
                    ()-> assertThat(favoriteArtist.getFanId()).isEqualTo(MEMBER_ID)
            );
        }

        @Test
        void 없는_아티스트로_등록할_수_없다() {
            // given
            long notExistArtist = 100;
            FavoriteArtistRequest request = new FavoriteArtistRequest(notExistArtist);

            // when then
            assertThatThrownBy(() -> artistService.registerFavoriteArtist(request, LOGIN_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(ArtistErrorCode.NOT_EXIST_ARTIST);
        }

        @Test
        void 한_명만_등록할_수_있다() {
            // given
            long beforeFavoriteArtist = 1L;
            long afterFavoriteArtist = 2L;
            artistService.registerFavoriteArtist(
                    new FavoriteArtistRequest(beforeFavoriteArtist), LOGIN_MEMBER
            );

            // when
            artistService.registerFavoriteArtist(
                    new FavoriteArtistRequest(afterFavoriteArtist), LOGIN_MEMBER
            );

            // then
            FavoriteArtist favoriteArtist = entityManager.find(FavoriteArtist.class, 1L);
            assertAll(
                    ()-> assertThat(favoriteArtist.getArtistId()).isEqualTo(afterFavoriteArtist),
                    ()-> assertThat(favoriteArtist.getFanId()).isEqualTo(MEMBER_ID)
            );
        }
    }

    @Nested
    @DisplayName("관심 아티스트를")
    class RegisterInterestArtistTest {

        @Test
        void 정상적으로_등록한다() {
            // given
            List<InterestArtistRequest> request = List.of(
                    new InterestArtistRequest(1L),
                    new InterestArtistRequest(2L),
                    new InterestArtistRequest(3L),
                    new InterestArtistRequest(4L),
                    new InterestArtistRequest(5L),
                    new InterestArtistRequest(6L),
                    new InterestArtistRequest(7L),
                    new InterestArtistRequest(8L),
                    new InterestArtistRequest(9L),
                    new InterestArtistRequest(10L)
            );

            // when
            artistService.registerInterestArtist(request, LOGIN_MEMBER);

            // then
            List<InterestArtist> interestArtists = entityManager.createQuery(
                    "select ia from InterestArtist ia", InterestArtist.class)
                    .getResultList();
            assertAll(
                    () -> assertThat(interestArtists)
                            .map(InterestArtist::getFanId)
                            .containsOnly(MEMBER_ID),
            () -> assertThat(interestArtists)
                    .map(InterestArtist::getArtistId)
                    .containsOnly(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)
            );
        }
    }
}