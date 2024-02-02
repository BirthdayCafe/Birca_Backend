package com.birca.bircabackend.command.artist.domain;

import org.springframework.data.repository.Repository;

public interface InterestArtistRepository extends Repository<InterestArtist, Long> {

    Long countByFanId(Long fanId);
}
