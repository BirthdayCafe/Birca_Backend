package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.FavoriteArtist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface FavoriteArtistQueryRepository extends Repository<FavoriteArtist, Long> {

    @Query("SELECT f.artistId FROM FavoriteArtist f WHERE f.fanId = :fanId")
    Long findArtistIdByFanId(Long fanId);
}
