package com.birca.bircabackend.command.like.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "uc_birthday_cafe_like_birthday_cafe_visitant",
        columnNames = {"birthdayCafeId", "visitantId"}
)})
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
