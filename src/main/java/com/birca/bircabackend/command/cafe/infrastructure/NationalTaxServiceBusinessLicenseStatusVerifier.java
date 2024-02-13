package com.birca.bircabackend.command.cafe.infrastructure;

import com.birca.bircabackend.command.cafe.application.BusinessLicenseStatusVerifier;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseStatusRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseStatusResponse;
import com.birca.bircabackend.common.ApiResponseExtractor;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.NOT_REGISTERED_BUSINESS_LICENSE_NUMBER;

@Component
@RequiredArgsConstructor
public class NationalTaxServiceBusinessLicenseStatusVerifier implements BusinessLicenseStatusVerifier {

    private static final String NOT_REGISTERED_BUSINESS_LICENSE_MESSAGE = "국세청에 등록되지 않은 사업자등록번호입니다.";

    private final NationalTaxServiceVerificationApi verificationApi;

    @Override
    public void verifyBusinessLicenseStatus(String businessLicenseNumber) {
        String licenseNumber = businessLicenseNumber.replaceAll("-", "");
        BusinessLicenseStatusRequest request = new BusinessLicenseStatusRequest(List.of(licenseNumber));
        ResponseEntity<BusinessLicenseStatusResponse> response = verificationApi.verifyBusinessLicenseStatus(request);
        BusinessLicenseStatusResponse businessLicenseStatus = ApiResponseExtractor.getBody(response);
        validateTaxType(businessLicenseStatus);
    }

    private void validateTaxType(BusinessLicenseStatusResponse businessLicenseStatusResponse) {
        String taxType = businessLicenseStatusResponse.getTaxType();
        if (taxType.equals(NOT_REGISTERED_BUSINESS_LICENSE_MESSAGE)) {
            throw BusinessException.from(NOT_REGISTERED_BUSINESS_LICENSE_NUMBER);
        }
    }
}
