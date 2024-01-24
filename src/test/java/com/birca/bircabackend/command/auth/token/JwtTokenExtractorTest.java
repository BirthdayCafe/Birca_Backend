package com.birca.bircabackend.command.auth.token;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JwtTokenExtractorTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Nested
    @DisplayName("Authorization 헤더에 값이")
    class ExtractTest {

        @Test
        void 있으면_토큰을_추출한다() {
            // given
            String expected = "mockToken.mockToken.mockToken";
            given(request.getHeader(HttpHeaders.AUTHORIZATION))
                    .willReturn("Bearer " + expected);

            // when
            Optional<String> actual = JwtTokenExtractor.extractToken(request);

            // then
            assertThat(actual).get().isEqualTo(expected);
        }

        @Test
        void 없으면_empty를_반환한다() {
            // given
            given(request.getHeader(HttpHeaders.AUTHORIZATION))
                    .willReturn(null);

            // when
            Optional<String> actual = JwtTokenExtractor.extractToken(request);

            // then
            assertThat(actual).isEmpty();
        }

    }
}
