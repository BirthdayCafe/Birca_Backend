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

    public static BirthdayCafeImage createDefaultImage(Long birthdayCafeId, String imageUrl) {
        return new BirthdayCafeImage(birthdayCafeId, imageUrl, false);
    }

    public static BirthdayCafeImage createMainImage(Long birthdayCafeId, String imageUrl) {
        return new BirthdayCafeImage(birthdayCafeId, imageUrl, true);
    }

    private BirthdayCafeImage(Long birthdayCafeId, String imageUrl, Boolean isMain) {
        this.birthdayCafeId = birthdayCafeId;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
    }

    public void updateMainImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
