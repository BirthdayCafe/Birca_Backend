package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BirthdayCafeLike extends BaseEntity {

    @Column(nullable = false)
    private Long birthdayCafeId;

    @Column(nullable = false)
    private Long visitantId;

    public BirthdayCafeLike(Long birthdayCafeId, Long visitantId) {
        this.birthdayCafeId = birthdayCafeId;
        this.visitantId = visitantId;
    }
}
