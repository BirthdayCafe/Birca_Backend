package com.birca.bircabackend.support.enviroment;

import com.birca.bircabackend.command.auth.token.JwtTokenProvider;
import com.birca.bircabackend.command.member.application.MemberService;
import com.birca.bircabackend.support.TestBearerTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@WebMvcTest
@Import({
        JwtTokenProvider.class,
        TestBearerTokenProvider.class
})
@AutoConfigureRestDocs
public class DocumentationTest {

    protected static final OperationRequestPreprocessor HOST_INFO = preprocessRequest(modifyUris()
            .scheme("https")
            .host("www.api.birca.com")
            .removePort(), prettyPrint()
    );

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TestBearerTokenProvider bearerTokenProvider;

    @MockBean
    protected MemberService memberService;
}
