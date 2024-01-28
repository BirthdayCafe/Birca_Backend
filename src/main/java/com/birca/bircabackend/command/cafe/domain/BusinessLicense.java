package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "UNIQUE_BUSINESS_LICENSE_CODE",
        columnNames = {"taxOfficeCode", "businessTypeCode", "serialCode", "validationCode"}
)})
@Getter
public class BusinessLicense extends BaseEntity {

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String cafeName;

    @Embedded
    private BusinessLicenseCode code;

    @Embedded
    private BusinessAddress address;

    @Column(nullable = false)
    @Lob
    private String imageUrl;
}
