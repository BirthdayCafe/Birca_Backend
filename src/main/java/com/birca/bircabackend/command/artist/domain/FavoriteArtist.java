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
public class FavoriteArtist extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long fanId;

    @Column(nullable = false)
    private Long artistId;

    public FavoriteArtist(Long fanId, Long artistId) {
        this.fanId = fanId;
        this.artistId = artistId;
    }

    public void changeArtist(Long artistId) {
        this.artistId = artistId;
    }
}
