package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import org.springframework.data.repository.Repository;

public interface ArtistGroupQueryRepository extends Repository<ArtistGroup, Long>, ArtistGroupDynamicRepository {
}
