package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "IDX_CAFE_NAME", columnList = "name"))
@Getter
public class Cafe extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long businessLicenseId;
  
    @Column(nullable = false)
    private Long ownerId;
  
    @Column(nullable = false)
    private String name;
}
