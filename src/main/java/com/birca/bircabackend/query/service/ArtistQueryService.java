package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.repository.ArtistQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistQueryService {

    private final ArtistQueryRepository artistQueryRepository;

    public List<ArtistResponse> findArtistByGroup(Long groupId) {
        return artistQueryRepository.findByGroupId(groupId)
                .stream()
                .map(ArtistResponse::new)
                .toList();
    }

    public List<ArtistResponse> findSoloArtists(PagingParams pagingParams) {
        return artistQueryRepository.queryPagedSoloArtistsSortByName(pagingParams)
                .stream()
                .map(ArtistResponse::new)
                .toList();
    }
}
