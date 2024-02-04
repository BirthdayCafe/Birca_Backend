package com.birca.bircabackend.support.enviroment;

import com.birca.bircabackend.support.TestBearerTokenProvider;
import com.birca.bircabackend.support.isolation.DatabaseIsolation;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

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
}
