package com.birca.bircabackend.command.artist.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface FavoriteArtistRepository extends Repository<FavoriteArtist, Long> {

    void save(FavoriteArtist favoriteArtist);

    Optional<FavoriteArtist> findByFanId(Long fanId);
}
