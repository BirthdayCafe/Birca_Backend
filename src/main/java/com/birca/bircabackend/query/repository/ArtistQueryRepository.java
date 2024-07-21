package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.query.dto.ArtistSearchResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ArtistQueryRepository extends Repository<Artist, Long>, ArtistDynamicRepository {

    List<Artist> findByGroupId(Long groupId);

    @Query("select new com.birca.bircabackend.query.dto.ArtistSearchResponse(a.id, a.name, a.imageUrl, ag.name) " +
            "from Artist a left join ArtistGroup ag on a.groupId = ag.id where a.name like %:name%")
    List<ArtistSearchResponse> findByName(String name);

    @Query("select a from Artist a where a.groupId is null")
    List<Artist> findAll();
}
