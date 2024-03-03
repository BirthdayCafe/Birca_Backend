package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.query.dto.ArtistSearchResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ArtistGroupQueryRepository extends Repository<ArtistGroup, Long>, ArtistGroupDynamicRepository {

    @Query("select new com.birca.bircabackend.query.dto.ArtistSearchResponse(a.id, a.name, a.imageUrl, ag.name) " +
            "from ArtistGroup ag join Artist a on a.groupId = ag.id where ag.name like %:name%")
    List<ArtistSearchResponse> findByName(String name);
}
