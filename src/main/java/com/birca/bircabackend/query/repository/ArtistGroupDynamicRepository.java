package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.query.dto.PagingParams;

import java.util.List;

public interface ArtistGroupDynamicRepository {

    List<ArtistGroup> queryPagedGroupsSortByName(PagingParams pagingParams);
}
