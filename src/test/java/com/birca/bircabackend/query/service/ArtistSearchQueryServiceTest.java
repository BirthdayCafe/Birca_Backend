package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.ArtistSearchResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/search-artist-fixture.sql")
class ArtistSearchQueryServiceTest extends ServiceTest {

    @Autowired
    private ArtistSearchQueryService artistSearchQueryService;

    @Nested
    @DisplayName("아티스트 검색 시")
    class SearchArtistTest {

        @Test
        void 겹치는_아티스트를_모두_검색한다() {
            // given
            String name = "마크";

            // when
            List<ArtistSearchResponse> actual = artistSearchQueryService.searchArtist(name);

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
            List<ArtistSearchResponse> actual = artistSearchQueryService.searchArtist(name);

            // then
            assertThat(actual)
                    .containsOnly(
                            new ArtistSearchResponse(17L, "아이유",
                                    "image17.com", null
                            )
                    );
        }

        @Test
        void 그룹명_전체로_검색한다() {
            // given
            String name = "방탄소년단";

            // when
            List<ArtistSearchResponse> actual = artistSearchQueryService.searchArtist(name);

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
                            ),
                            new ArtistSearchResponse(7L, "슈가",
                                    "image7.com", "방탄소년단"
                            )
                    );
        }

        @Test
        void 그룹명_일부로_검색한다() {
            // given
            String name = "소년";

            // when
            List<ArtistSearchResponse> actual = artistSearchQueryService.searchArtist(name);

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
                            ),
                            new ArtistSearchResponse(7L, "슈가",
                                    "image7.com", "방탄소년단"
                            )
                    );
        }

        @Test
        void 중복으로_검색된_결과는_제거한다() {
            // given
            String name = "아이돌";

            // when
            List<ArtistSearchResponse> actual = artistSearchQueryService.searchArtist(name);

            assertThat(actual)
                    .containsOnly(new ArtistSearchResponse(23L, "아이돌1",
                                    "image23.com", "아이돌그룹"),
                                  new ArtistSearchResponse(24L, "아이돌2",
                                    "image24.com", "아이돌그룹")
                    );
        }
    }
}
