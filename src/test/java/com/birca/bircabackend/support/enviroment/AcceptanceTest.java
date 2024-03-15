package com.birca.bircabackend.support.enviroment;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.birca.bircabackend.support.TestBearerTokenProvider;
import com.birca.bircabackend.support.isolation.DatabaseIsolation;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DatabaseIsolation
public class AcceptanceTest extends MockOAuthEnvironment {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    protected TestBearerTokenProvider bearerTokenProvider;

    private static final String URL ="https://s3.ap-northeast-2.amazonaws.com/travel-with-me-fileupload/image/example.png";

    @Autowired
    private AmazonS3 amazonS3;

    @BeforeEach
    void mockAwsS3() throws MalformedURLException {
        given(amazonS3.putObject(any(PutObjectRequest.class)))
                .willReturn(new PutObjectResult());
        given(amazonS3.getUrl(any(), any()))
                .willReturn(new URL(URL));
    }
}
