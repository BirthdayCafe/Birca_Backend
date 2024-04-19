package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExtraDayOff extends BaseEntity {

    @Column(nullable = false)
    private Long cafeId;

    @Column(nullable = false)
    private LocalDateTime dayOffDate;

    public ExtraDayOff(Long cafeId, LocalDateTime dayOffDate) {
        this.cafeId = cafeId;
        this.dayOffDate = dayOffDate;
    }
}
