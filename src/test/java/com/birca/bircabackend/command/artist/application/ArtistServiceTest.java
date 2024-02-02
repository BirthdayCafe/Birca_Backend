package com.birca.bircabackend.command.artist.application;

import com.birca.bircabackend.command.artist.domain.FavoriteArtist;
import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
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
    class RegisterTest {

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
    }
}
