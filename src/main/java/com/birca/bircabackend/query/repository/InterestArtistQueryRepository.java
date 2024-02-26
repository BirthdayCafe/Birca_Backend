package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.InterestArtist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface InterestArtistQueryRepository extends Repository<InterestArtist, Long> {

    @Query("SELECT a FROM Artist a JOIN InterestArtist i ON a.id = i.artistId WHERE i.fanId = :fanId")
    List<Artist> findInterestArtistsByFanId(Long fanId);
}
