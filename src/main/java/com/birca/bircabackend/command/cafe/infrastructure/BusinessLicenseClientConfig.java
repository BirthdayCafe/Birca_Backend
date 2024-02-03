package com.birca.bircabackend.command.cafe.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY;

@Configuration
public class BusinessLicenseClientConfig {

    @Value("${ocr.invoke-url}")
    private String invokeUrl;

    @Value("${service-key.key}")
    private String serviceKeyUrl;

    @Bean
    public WebClient businessLicenseOcrClient() {
        return WebClient.builder()
                .baseUrl(invokeUrl)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient verifyBusinessNumberClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(serviceKeyUrl);
        factory.setEncodingMode(VALUES_ONLY);
        return WebClient.builder()
                .uriBuilderFactory(factory)
                .build();
    }
}
