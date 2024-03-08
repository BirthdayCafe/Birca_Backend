package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "uc_cafe_businesslicenseid",
        columnNames = {"businessLicenseId"}
)})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cafe extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long businessLicenseId;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String name;
}
