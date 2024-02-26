package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.FavoriteArtist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface FavoriteArtistQueryRepository extends Repository<FavoriteArtist, Long> {

    @Query("SELECT a FROM Artist a JOIN FavoriteArtist fa ON a.id = fa.artistId WHERE fa.fanId = :fanId")
    Optional<Artist> findFavoriteArtistByFanId(Long fanId);
}
