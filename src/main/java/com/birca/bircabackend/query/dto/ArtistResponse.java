package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.Artist;

public record ArtistResponse(
        Long artistId,
        String artistName,
        String artistImage
) {

    public ArtistResponse(Artist artist) {
        this(artist.getId(), artist.getName(), artist.getImageUrl());
    }
}
