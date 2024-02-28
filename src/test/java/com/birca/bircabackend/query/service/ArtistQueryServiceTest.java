package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.dto.ArtistSearchResponse;
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
class ArtistQueryServiceTest extends ServiceTest {

    @Autowired
    private ArtistQueryService artistQueryService;

    @Nested
    @DisplayName("아티스트 그룹 멤버를")
    class FindGroupMemberTest {

        @Test
        void 조회_한다() {
            // given
            Long btsId = 6L;

            // when
            List<ArtistResponse> actual = artistQueryService.findArtistByGroup(btsId);

            // then
            assertThat(actual)
                    .containsOnly(
                            new ArtistResponse(5L, "뷔", "image5.com"),
                            new ArtistResponse(1L, "석진", "image1.com"),
                            new ArtistResponse(7L, "슈가", "image7.com"),
                            new ArtistResponse(2L, "정국", "image2.com"),
                            new ArtistResponse(3L, "제이홉", "image3.com"),
                            new ArtistResponse(6L, "지민", "image6.com"),
                            new ArtistResponse(4L, "RM", "image4.com")
                    );
        }
    }

    @Nested
    @DisplayName("솔로 아티스트 목록 조회 시")
    class FindSoloArtistsTest {

        private final PagingParams pagingParams = new PagingParams();

        @Test
        void 필요한_필드를_모두_조회한다() {
            // when
            List<ArtistResponse> actual = artistQueryService.findSoloArtists(pagingParams);

            // then
            assertThat(actual.get(0))
                    .isEqualTo(new ArtistResponse(13L, "김범수", "image13.com"));
        }

        @Test
        void 커서와_사이즈가_없는_경우를_조회한다() {
            // when
            List<ArtistResponse> actual = artistQueryService.findSoloArtists(pagingParams);

            // then
            assertThat(actual)
                    .containsExactly(
                            new ArtistResponse(13L, "김범수", "image13.com"),
                            new ArtistResponse(14L, "로이킴", "image14.com"),
                            new ArtistResponse(15L, "박재정", "image15.com"),
                            new ArtistResponse(16L, "성시경", "image16.com"),
                            new ArtistResponse(17L, "아이유", "image17.com"),
                            new ArtistResponse(18L, "윤종신", "image18.com")
                    );
        }

        @Test
        void 사이즈만큼_조회한다() {
            // given
            pagingParams.setSize(8);

            // when
            List<ArtistResponse> actual = artistQueryService.findSoloArtists(pagingParams);

            // then
            assertThat(actual)
                    .map(ArtistResponse::artistId)
                    .containsExactly(13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L);
        }

        @Test
        void 커서_이후만큼_조회한다() {
            // given
            pagingParams.setCursor(14L);

            // when
            List<ArtistResponse> actual = artistQueryService.findSoloArtists(pagingParams);

            // then
            assertThat(actual)
                    .containsExactly(
                            new ArtistResponse(15L, "박재정", "image15.com"),
                            new ArtistResponse(16L, "성시경", "image16.com"),
                            new ArtistResponse(17L, "아이유", "image17.com"),
                            new ArtistResponse(18L, "윤종신", "image18.com"),
                            new ArtistResponse(19L, "임한별", "image19.com"),
                            new ArtistResponse(20L, "하동균", "image20.com")
                    );
        }
    }

    @Nested
    @DisplayName("아티스트 검색 시")
    @Sql("/fixture/search-artist-fixture.sql")
    class SearchArtistTest {

        @Test
        void 겹치는_아티스트를_모두_검색한다() {
            // given
            String name = "마크";

            // when
            List<ArtistSearchResponse> actual = artistQueryService.searchArtist(name);

            // then
            assertThat(actual)
                    .containsOnly(
                            new ArtistSearchResponse(21L, "마크",
                                    "image21.com", "NCT127"
                            ),
                            new ArtistSearchResponse(22L, "마크",
                                    "image22.com", "갓세븐"
                            )
                    );
        }

        @Test
        void 솔로_아티스트는_그룹명이_null이다() {
            // given
            String name = "아이유";

            // when
            List<ArtistSearchResponse> actual = artistQueryService.searchArtist(name);

            // then
            assertThat(actual)
                    .containsOnly(
                            new ArtistSearchResponse(17L, "아이유",
                                    "image17.com", null
                            )
                    );
        }

        @Test
        void 그룹명으로_검색한다() {
            // given
            String name = "방탄소년단";

            // when
            List<ArtistSearchResponse> actual = artistQueryService.searchArtist(name);

            // then
            assertThat(actual)
                    .containsOnly(
                            new ArtistSearchResponse(1L, "석진",
                                    "image1.com", "방탄소년단"
                            ),
                            new ArtistSearchResponse(2L, "정국",
                                    "image2.com", "방탄소년단"
                            ),
                            new ArtistSearchResponse(3L, "제이홉",
                                    "image3.com", "방탄소년단"
                            ),
                            new ArtistSearchResponse(4L, "RM",
                                    "image4.com", "방탄소년단"
                            ),
                            new ArtistSearchResponse(5L, "뷔",
                                    "image5.com", "방탄소년단"
                            ),
                            new ArtistSearchResponse(6L, "지민",
                                    "image6.com", "방탄소년단"
                            ), new ArtistSearchResponse(7L, "슈가",
                                    "image7.com", "방탄소년단"
                            )
                    );
        }
    }
}
