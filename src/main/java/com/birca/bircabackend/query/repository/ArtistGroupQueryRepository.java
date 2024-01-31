package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.query.dto.PagingParams;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ArtistGroupQueryRepository extends Repository<ArtistGroup, Long> {

    List<ArtistGroup> queryPagedGroups(PagingParams pagingParams);
}
