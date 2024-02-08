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

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private Integer uploadCount;

    public Integer incrementUploadCount() {
        return ++uploadCount;
    }

    public OcrRequestHistory(Long ownerId, Integer uploadCount) {
        this.ownerId = ownerId;
        this.uploadCount = uploadCount;
    }
}
