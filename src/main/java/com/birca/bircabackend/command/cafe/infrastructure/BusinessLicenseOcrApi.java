package com.birca.bircabackend.command.cafe.infrastructure;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "businessLicenseOcrFeignClient", url = "${ocr.invoke-url}")
public interface BusinessLicenseOcrApi {

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    BusinessLicenseOcrResponse performBusinessLicenseOcr(
            @RequestBody BusinessLicenseOcrRequest request,
            @RequestHeader("X-OCR-SECRET") String ocrServiceKey);
}
