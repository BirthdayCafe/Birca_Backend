package com.birca.bircabackend.command.cafe.infrastructure;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseStatusRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseStatusResponse;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.BUSINESS_LICENSE_STATUS_ERROR;
import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.NOT_REGISTERED_BUSINESS_LICENSE_NUMBER;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class BusinessLicenseVerificationClientImpl implements BusinessLicenseVerificationClient {

    private final WebClient verifyBusinessNumberClient;
    private static final String NOT_REGISTERED_BUSINESS_LICENSE_MESSAGE = "국세청에 등록되지 않은 사업자등록번호입니다.";

    @Override
    public void verifyBusinessLicenseStatus(String businessLicenseNumber) {
        BusinessLicenseStatusResponse response = requestBusinessLicenseStatus(businessLicenseNumber);
        validateTaxType(response);
    }

    private BusinessLicenseStatusResponse requestBusinessLicenseStatus(String businessLicenseNumber) {
        String licenseNumber = businessLicenseNumber.replaceAll("-", "");
        BusinessLicenseStatusRequest request = new BusinessLicenseStatusRequest(List.of(licenseNumber));
        return verifyBusinessNumberClient.post()
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BusinessLicenseStatusResponse.class)
                .onErrorMap(error -> BusinessException.from(BUSINESS_LICENSE_STATUS_ERROR))
                .block();
    }

    private void validateTaxType(BusinessLicenseStatusResponse businessLicenseStatusResponse) {
        String taxType = businessLicenseStatusResponse.getTaxType();
        if (taxType.equals(NOT_REGISTERED_BUSINESS_LICENSE_MESSAGE)) {
            throw BusinessException.from(NOT_REGISTERED_BUSINESS_LICENSE_NUMBER);
        }
    }
}
