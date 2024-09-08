package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CafeImage extends BaseEntity {

    @Column(nullable = false)
    private Long cafeId;

    @Column(nullable = false)
    private String imageUrl;

    public CafeImage(Long cafeId, String imageUrl) {
        this.cafeId = cafeId;
        this.imageUrl = imageUrl;
    }
}
