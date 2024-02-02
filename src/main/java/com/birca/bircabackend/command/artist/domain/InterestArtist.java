package com.birca.bircabackend.command.artist.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InterestArtist extends BaseEntity {

    public static final int REGISTER_LIMIT = 10;

    @Column(nullable = false)
    private Long fanId;

    @Column(nullable = false)
    private Long artistId;

    public InterestArtist(Long fanId, Long artistId) {
        this.fanId = fanId;
        this.artistId = artistId;
    }
}
