package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.command.cafe.domain.value.DayOfWeek;
import com.birca.bircabackend.command.cafe.domain.value.IntervalType;
import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RegularDayOff extends BaseEntity {

    @Column(nullable = false)
    private Long cafeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntervalType intervalType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayofWeek;

    public static RegularDayOff create(Long cafeId, IntervalType intervalType, DayOfWeek dayofWeek) {
        return new RegularDayOff(cafeId, intervalType, dayofWeek);
    }

    public RegularDayOff(Long cafeId, IntervalType intervalType, DayOfWeek dayofWeek) {
        this.cafeId = cafeId;
        this.intervalType = intervalType;
        this.dayofWeek = dayofWeek;
    }
}
