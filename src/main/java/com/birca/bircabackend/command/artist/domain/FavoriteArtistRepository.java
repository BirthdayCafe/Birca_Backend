package com.birca.bircabackend.command.artist.domain;

import org.springframework.data.repository.Repository;

public interface FavoriteArtistRepository extends Repository<FavoriteArtist, Long> {

    void save(FavoriteArtist favoriteArtist);
}
