package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.*;
import static java.lang.String.valueOf;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BusinessLicenseCode {

    private static final int TAX_OFFICE_CODE_LENGTH = 3;
    private static final int BUSINESS_TYPE_CODE_LENGTH = 2;
    private static final int SERIAL_CODE_LENGTH = 5;
    private static final int BUSINESS_LICENSE_PARTS = 3;

    @Column(nullable = false)
    private String taxOfficeCode;

    @Column(nullable = false)
    private String businessTypeCode;

    @Column(nullable = false)
    private String serialCode;

    public BusinessLicenseCode(String businessLicenseNumber) {
        String[] codes = businessLicenseNumber.split("-");
        validateBusinessLicenseNumberForm(codes);
        this.taxOfficeCode = codes[0];
        this.businessTypeCode = codes[1];
        this.serialCode = codes[2];
    }

    private void validateBusinessLicenseNumberForm(String[] codes) {
        if (codes.length != BUSINESS_LICENSE_PARTS) {
            throw BusinessException.from(INVALID_BUSINESS_LICENSE_NUMBER_FORM);
        }
        if (codes[0].length() != TAX_OFFICE_CODE_LENGTH) {
            throw BusinessException.from(INVALID_TAX_OFFICE_CODE_LENGTH);
        }
        if (codes[1].length() != BUSINESS_TYPE_CODE_LENGTH) {
            throw BusinessException.from(INVALID_BUSINESS_TYPE_CODE_LENGTH);
        }
        if (codes[2].length() != SERIAL_CODE_LENGTH) {
            throw BusinessException.from(INVALID_SERIAL_CODE_LENGTH);
        }
    }
}
