package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
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

    public ArtistResponse findFavoriteArtist(LoginMember loginMember) {
        return favoriteArtistQueryRepository.findFavoriteArtistByFanId(loginMember.id())
                .map(ArtistResponse::new)
                .orElseGet(ArtistResponse::createEmpty);
    }
}
