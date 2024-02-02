package com.birca.bircabackend.command.artist.domain;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface InterestArtistRepository extends Repository<InterestArtist, Long> {

    List<InterestArtist> findByFanId(Long fanId);
}
