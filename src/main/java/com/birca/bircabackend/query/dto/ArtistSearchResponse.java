package com.birca.bircabackend.query.dto;

public record ArtistSearchResponse(
        Long artistId,
        String artistName,
        String artistImageUrl,
        String groupName
) {
}

