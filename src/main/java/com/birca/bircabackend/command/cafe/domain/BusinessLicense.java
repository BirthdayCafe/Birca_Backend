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
        columnNames = {"taxOfficeCode", "businessTypeCode", "serialCode"}
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

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Lob
    private String imageUrl;

    @Column(nullable = false)
    private Boolean registrationApproved;

    public static BusinessLicense createBusinessLicense(Long ownerId, String ownerName, String cafeName,
                                                        BusinessLicenseCode code, String address,
                                                        String imageUrl) {
        return new BusinessLicense(ownerId, ownerName, cafeName, code, address, imageUrl);
    }

    private BusinessLicense(Long ownerId, String ownerName, String cafeName,
                            BusinessLicenseCode code, String address, String imageUrl) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.cafeName = cafeName;
        this.code = code;
        this.address = address;
        this.imageUrl = imageUrl;
        registrationApproved = false;
    }
}
