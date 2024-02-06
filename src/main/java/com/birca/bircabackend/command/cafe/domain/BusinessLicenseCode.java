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

    @Column(nullable = false)
    private Integer taxOfficeCode;

    @Column(nullable = false)
    private Integer businessTypeCode;

    @Column(nullable = false)
    private Integer serialCode;

    public static BusinessLicenseCode createBusinessLicenseCode(Integer taxOfficeCode,
                                                                Integer businessTypeCode,
                                                                Integer serialCode) {
        return new BusinessLicenseCode(taxOfficeCode, businessTypeCode, serialCode);
    }

    private BusinessLicenseCode(Integer taxOfficeCode, Integer businessTypeCode, Integer serialCode) {
        validateCode(taxOfficeCode, businessTypeCode, serialCode);
        this.taxOfficeCode = taxOfficeCode;
        this.businessTypeCode = businessTypeCode;
        this.serialCode = serialCode;
    }

    private void validateCode(Integer taxOfficeCode, Integer businessTypeCode, Integer serialCode) {
        if (getCodeLength(taxOfficeCode) != 3) {
            throw BusinessException.from(INVALID_TAX_OFFICE_CODE_LENGTH);
        }
        if (getCodeLength(businessTypeCode) != 2) {
            throw BusinessException.from(INVALID_BUSINESS_TYPE_CODE_LENGTH);
        }
        if (getCodeLength(serialCode) != 5) {
            throw BusinessException.from(INVALID_SERIAL_CODE_LENGTH);
        }
    }

    private int getCodeLength(Integer code) {
        return valueOf(code).length();
    }
}
