package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.Artist;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ArtistQueryRepository extends Repository<Artist, Long> {

    List<Artist> findByGroupId(Long groupId);
}
