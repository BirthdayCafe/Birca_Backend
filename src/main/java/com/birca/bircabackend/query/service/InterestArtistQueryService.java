package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.exception.ArtistErrorCode;
import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.repository.config.InterestArtistQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestArtistQueryService {

    private final InterestArtistQueryRepository interestArtistQueryRepository;
    private final EntityUtil entityUtil;

    public List<ArtistResponse> findInterestArtists(LoginMember loginMember) {
        List<Long> artistIds = interestArtistQueryRepository.findInterestArtistIdsByFanId(loginMember.id());
        return artistIds.stream()
                .map(artistId -> entityUtil.getEntity(Artist.class, artistId, ArtistErrorCode.NOT_EXIST_ARTIST))
                .map(ArtistResponse::new)
                .toList();
    }
}
