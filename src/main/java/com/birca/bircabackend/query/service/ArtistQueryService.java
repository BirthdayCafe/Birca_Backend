package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.ArtistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistQueryService {

    public List<ArtistResponse> findArtistByGroup(Long groupId) {
        return null;
    }
}
