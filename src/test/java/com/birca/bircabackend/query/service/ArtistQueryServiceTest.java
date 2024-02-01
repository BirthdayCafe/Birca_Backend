package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.ArtistResponse;
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
}
