package com.birca.bircabackend.command.cafe.infrastructure;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseStatusRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseStatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@FeignClient(name = "businessLicenseVerificationFeignClient", url = "${service-key.key}")
public interface BusinessLicenseVerificationApi {
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    BusinessLicenseStatusResponse verifyBusinessLicenseStatus(@RequestBody BusinessLicenseStatusRequest request);
}
