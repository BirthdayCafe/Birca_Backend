package com.birca.bircabackend.command.artist.domain;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface ArtistRepository extends Repository<Artist, Long> {

    Boolean existsById(Long id);

    Long countByIdIn(List<Long> id);
}
