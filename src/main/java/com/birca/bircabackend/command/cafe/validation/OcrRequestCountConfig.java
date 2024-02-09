package com.birca.bircabackend.command.cafe.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class OcrRequestCountConfig implements WebMvcConfigurer {

    private final OcrRequestCountInterceptor ocrRequestCountInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ocrRequestCountInterceptor)
                .order(2)
                .addPathPatterns("/api/v1/cafes/license-read", "/test-upload-count");
    }
}
