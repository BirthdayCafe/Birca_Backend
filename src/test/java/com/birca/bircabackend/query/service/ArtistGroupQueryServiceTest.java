package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.ArtistGroupResponse;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/artist-fixture.sql")
class ArtistGroupQueryServiceTest extends ServiceTest {

    @Autowired
    private ArtistGroupQueryService artistGroupQueryService;

    @Nested
    @DisplayName("아티스트 그룹 목록 조회 시")
    class GetGroupsTest {

        private final PagingParams pagingParams = new PagingParams();

        @Test
        void 필요한_필드를_모두_조회한다() {
            // when
            List<ArtistGroupResponse> actual = artistGroupQueryService.findGroups(pagingParams);

            // then
            assertThat(actual.get(0))
                    .isEqualTo(new ArtistGroupResponse(5L, "뉴진스", "image5.com"));
        }

        @Test
        void 커서와_사이즈가_없는_경우를_조회한다() {
            // when
            List<ArtistGroupResponse> actual = artistGroupQueryService.findGroups(pagingParams);

            // then
            assertThat(actual)
                    .map(ArtistGroupResponse::groupId)
                    .containsExactly(5L, 6L, 7L, 8L, 2L, 11L);
        }

        @Test
        void 사이즈만큼_조회한다() {
            // given
            pagingParams.setSize(10);

            // when
            List<ArtistGroupResponse> actual = artistGroupQueryService.findGroups(pagingParams);

            // then
            assertThat(actual)
                    .map(ArtistGroupResponse::groupId)
                    .containsExactly(5L, 6L, 7L, 8L, 2L, 11L, 12L, 3L, 1L, 9L);
        }

        @Test
        void 커서_이후만큼_조회한다() {
            // given
            pagingParams.setCursor(11L);

            // when
            List<ArtistGroupResponse> actual = artistGroupQueryService.findGroups(pagingParams);

            // then
            assertThat(actual)
                    .map(ArtistGroupResponse::groupId)
                    .containsExactly(12L, 3L, 1L, 9L, 4L, 10L);
        }
    }
}
