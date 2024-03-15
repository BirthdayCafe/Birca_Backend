package com.birca.bircabackend.support.enviroment;

import com.birca.bircabackend.support.isolation.DatabaseIsolation;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DatabaseIsolation
public class ServiceTest extends MockOAuthEnvironment {
}
