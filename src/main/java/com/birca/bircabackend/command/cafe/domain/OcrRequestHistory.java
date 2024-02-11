package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.INVALID_UPLOAD_COUNT;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OcrRequestHistory extends BaseEntity {

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private Integer uploadCount;

    private static final int DEFAULT_UPLOAD_COUNT = 0;

    public Integer incrementUploadCount() {
        return ++uploadCount;
    }

    public OcrRequestHistory(Long ownerId) {
        this.ownerId = ownerId;
        this.uploadCount = DEFAULT_UPLOAD_COUNT;
    }
}
