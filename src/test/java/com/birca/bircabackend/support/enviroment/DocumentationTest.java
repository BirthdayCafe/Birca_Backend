package com.birca.bircabackend.support.enviroment;

import com.birca.bircabackend.command.artist.application.ArtistService;
import com.birca.bircabackend.command.auth.application.token.JwtParseUtil;
import com.birca.bircabackend.command.birca.application.BirthdayCafeService;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseFacade;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseService;
import com.birca.bircabackend.command.auth.application.AuthFacade;
import com.birca.bircabackend.command.auth.application.token.JwtTokenProvider;
import com.birca.bircabackend.command.member.application.MemberService;
import com.birca.bircabackend.common.exception.ErrorCode;
import com.birca.bircabackend.common.log.TimeLogTemplate;
import com.birca.bircabackend.query.service.ArtistGroupQueryService;
import com.birca.bircabackend.query.service.ArtistQueryService;
import com.birca.bircabackend.query.service.FavoriteArtistQueryService;
import com.birca.bircabackend.query.service.MemberQueryService;
import com.birca.bircabackend.support.TestBearerTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@WebMvcTest
@Import({
        JwtTokenProvider.class,
        JwtParseUtil.class,
        TestBearerTokenProvider.class,
        TimeLogTemplate.class
})
@AutoConfigureRestDocs
public class DocumentationTest {

    protected static final OperationRequestPreprocessor HOST_INFO = preprocessRequest(modifyUris()
            .scheme("https")
            .host("www.api.birca.com")
            .removePort(), prettyPrint()
    );

    protected static final OperationResponsePreprocessor DOCUMENT_RESPONSE = preprocessResponse(prettyPrint());

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TestBearerTokenProvider bearerTokenProvider;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected MemberQueryService memberQueryService;

    @MockBean
    protected ArtistGroupQueryService artistGroupQueryService;

    @MockBean
    protected ArtistQueryService artistQueryService;
  
    @MockBean
    protected ArtistService artistService;

    @MockBean
    protected BusinessLicenseService businessLicenseService;

    @MockBean
    protected AuthFacade authFacade;

    @MockBean
    protected BirthdayCafeService birthdayCafeService;

    @MockBean
    protected BusinessLicenseFacade businessLicenseFacade;

    @MockBean
    protected FavoriteArtistQueryService favoriteArtistQueryService;

    protected List<FieldDescriptor> getErrorDescriptor(ErrorCode[] errorCodes) {
        return Arrays.stream(errorCodes)
                .flatMap(errorCode -> Stream.of(
                        fieldWithPath(errorCode.getValue() + ".httpStatus").description(errorCode.getHttpStatusCode()),
                        fieldWithPath(errorCode.getValue() + ".message").description(errorCode.getMessage())
                )).toList();
    }
}
