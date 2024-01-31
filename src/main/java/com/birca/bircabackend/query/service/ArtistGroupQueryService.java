package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.ArtistGroupResponse;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.repository.ArtistGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistGroupQueryService {

    private final ArtistGroupQueryRepository artistGroupQueryRepository;

    public List<ArtistGroupResponse> findGroups(PagingParams pagingParams) {
        return artistGroupQueryRepository.queryPagedGroupsSortByName(pagingParams)
                .stream()
                .map(ArtistGroupResponse::new)
                .toList();
    }
}
