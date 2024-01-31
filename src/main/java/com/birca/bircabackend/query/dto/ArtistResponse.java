package com.birca.bircabackend.query.dto;

public record ArtistResponse(
        Long artistId,
        String artistName,
        String artistImage
) {
}
