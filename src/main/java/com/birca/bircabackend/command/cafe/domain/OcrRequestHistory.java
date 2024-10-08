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
public class OcrRequestHistory extends BaseEntity {

    private static final int DEFAULT_UPLOAD_COUNT = 0;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private Integer uploadCount;

    public Integer increaseUploadCount() {
        return ++uploadCount;
    }

    public OcrRequestHistory(Long ownerId) {
        this.ownerId = ownerId;
        this.uploadCount = DEFAULT_UPLOAD_COUNT;
    }
}
