package com.birca.bircabackend.command.cafe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Integer validationCode;
}
