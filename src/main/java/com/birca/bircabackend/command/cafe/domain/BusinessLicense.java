package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
