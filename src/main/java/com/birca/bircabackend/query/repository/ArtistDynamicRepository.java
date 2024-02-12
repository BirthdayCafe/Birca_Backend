package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.query.dto.PagingParams;

import java.util.List;

public interface ArtistDynamicRepository {

    List<Artist> queryPagedSoloArtistsSortByName(PagingParams pagingParams);
}
