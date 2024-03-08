package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.INVALID_BUSINESS_LICENSE_NUMBER;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BusinessLicenseCode {

    private static final Pattern VALID_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{5}$");

    @Column(nullable = false)
    private String taxOfficeCode;

    @Column(nullable = false)
    private String businessTypeCode;

    @Column(nullable = false)
    private String serialCode;

    public static BusinessLicenseCode from(String businessLicenseNumber) {
        return new BusinessLicenseCode(businessLicenseNumber);
    }

    private BusinessLicenseCode(String businessLicenseNumber) {
        validateBusinessLicenseNumber(businessLicenseNumber);
        String[] codes = businessLicenseNumber.split("-");
        this.taxOfficeCode = codes[0];
        this.businessTypeCode = codes[1];
        this.serialCode = codes[2];
    }

    private void validateBusinessLicenseNumber(String businessLicenseNumber) {
        if (!VALID_PATTERN.matcher(businessLicenseNumber).matches()) {
            throw BusinessException.from(INVALID_BUSINESS_LICENSE_NUMBER);
        }
    }
}
