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
public class BirthdayCafeImage extends BaseEntity {

    @Column(nullable = false)
    private Long birthdayCafeId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isMain;

    public BirthdayCafeImage(Long birthdayCafeId, String imageUrl) {
        this.birthdayCafeId = birthdayCafeId;
        this.imageUrl = imageUrl;
        this.isMain = false;
    }
}
