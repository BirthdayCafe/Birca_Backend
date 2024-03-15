package com.birca.bircabackend.support.enviroment;

import com.amazonaws.services.s3.AmazonS3;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockAwsS3Config {

    @Bean
    @Primary
    public AmazonS3 mockAmazonS3() {
        return Mockito.mock(AmazonS3.class);
    }
}
