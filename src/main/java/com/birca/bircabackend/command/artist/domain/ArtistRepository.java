package com.birca.bircabackend.command.artist.domain;

import org.springframework.data.repository.Repository;

public interface ArtistRepository extends Repository<Artist, Long> {

    Boolean existsById(Long id);
}
