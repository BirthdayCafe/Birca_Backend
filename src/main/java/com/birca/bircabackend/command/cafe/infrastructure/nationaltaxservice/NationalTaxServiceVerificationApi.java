package com.birca.bircabackend.command.cafe.infrastructure.nationaltaxservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@FeignClient(name = "nationalTaxServiceVerification", url = "${service-key.key}")
public interface NationalTaxServiceVerificationApi {

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<BusinessLicenseStatusResponse> verifyBusinessLicenseStatus(
            @RequestBody BusinessLicenseStatusRequest request);
}
