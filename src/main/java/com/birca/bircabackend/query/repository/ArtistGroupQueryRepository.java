package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.query.dto.ArtistSearchResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ArtistGroupQueryRepository extends Repository<ArtistGroup, Long>, ArtistGroupDynamicRepository {

    List<ArtistGroup> findAll();
}
