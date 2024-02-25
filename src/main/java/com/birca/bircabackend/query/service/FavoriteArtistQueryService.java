package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.exception.ArtistErrorCode;
import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.repository.FavoriteArtistQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteArtistQueryService {

    private final FavoriteArtistQueryRepository favoriteArtistQueryRepository;
    private final EntityUtil entityUtil;

    public ArtistResponse findFavoriteArtist(LoginMember loginMember) {
        Long artistId = favoriteArtistQueryRepository.findArtistIdByFanId(loginMember.id());
        Artist artist = entityUtil.getEntity(Artist.class, artistId, ArtistErrorCode.NOT_EXIST_ARTIST);
        return new ArtistResponse(artist);
    }
}
