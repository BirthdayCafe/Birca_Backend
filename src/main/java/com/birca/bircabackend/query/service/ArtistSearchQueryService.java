package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.ArtistSearchResponse;
import com.birca.bircabackend.query.repository.ArtistGroupQueryRepository;
import com.birca.bircabackend.query.repository.ArtistQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistSearchQueryService {

    private final ArtistQueryRepository artistQueryRepository;
    private final ArtistGroupQueryRepository artistGroupQueryRepository;

    public List<ArtistSearchResponse> searchArtist(String name) {
        List<ArtistSearchResponse> groupResults = artistGroupQueryRepository.findByName(name);
        List<ArtistSearchResponse> artistResults = artistQueryRepository.findByName(name);
        return Stream.concat(groupResults.stream(), artistResults.stream())
                .distinct()
                .toList();
    }
}
