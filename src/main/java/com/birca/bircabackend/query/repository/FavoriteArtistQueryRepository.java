package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.FavoriteArtist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface FavoriteArtistQueryRepository extends Repository<FavoriteArtist, Long> {

    @Query("SELECT a FROM Artist a WHERE a.id = (SELECT i.artistId FROM FavoriteArtist i WHERE i.fanId = :fanId)")
    Optional<Artist> findArtistIdByFanId(Long fanId);
}
