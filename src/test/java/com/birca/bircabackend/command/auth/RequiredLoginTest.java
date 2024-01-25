package com.birca.bircabackend.command.auth;

import com.birca.bircabackend.command.auth.exception.AuthErrorCode;
import com.birca.bircabackend.command.auth.token.JwtTokenProvider;
import com.birca.bircabackend.command.auth.token.TokenPayload;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(JwtTokenProvider.class)
class RequiredLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("로그인이 필요한 요청에서")
    class LoginTest {

        @Test
        void 유효한_토큰을_검증한다() throws Exception {
            // given
            Long memberId = 1L;
            String validToken = jwtTokenProvider.createToken(new TokenPayload(memberId));

            // when
            ResultActions result = mockMvc.perform(get("/test-login")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken));

            // then
            result.andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(String.valueOf(memberId)));
        }

        @Test
        void 토큰이_없는_경우를_검증한다() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/test-login"));

            // then
            result.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("errorCode", AuthErrorCode.NOT_EXISTS_TOKEN).exists());
        }

        @Test
        void 토큰이_유효하지_않은_경우를_검증한다() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/test-login")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer invalidToken.inValidToken.inValidToken"));

            // then
            result.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("errorCode", AuthErrorCode.INVALID_EXISTS_TOKEN).exists());
        }
    }
}
