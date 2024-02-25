package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.InterestArtist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface InterestArtistQueryRepository extends Repository<InterestArtist, Long> {

    @Query("SELECT i.artistId FROM InterestArtist i WHERE i.fanId = :fanId")
    List<Long> findInterestArtistIdsByFanId(Long fanId);
}
