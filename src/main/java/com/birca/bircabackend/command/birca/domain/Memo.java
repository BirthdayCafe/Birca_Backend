package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Memo extends BaseEntity {

    @Column(nullable = false)
    private Long birthdayCafeId;

    @Lob
    private String content;

    public Memo(Long birthdayCafeId, String content) {
        this.birthdayCafeId = birthdayCafeId;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
