package com.birca.bircabackend.command.cafe;

import com.birca.bircabackend.command.cafe.validation.OcrRequestLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class OcrRequestLimitConfig implements WebMvcConfigurer {

    private final OcrRequestLimitInterceptor ocrRequestLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ocrRequestLimitInterceptor);
    }
}
