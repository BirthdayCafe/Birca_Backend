package com.birca.bircabackend.command.artist.application;

import com.birca.bircabackend.command.artist.domain.ArtistRepository;
import com.birca.bircabackend.command.artist.domain.FavoriteArtist;
import com.birca.bircabackend.command.artist.domain.FavoriteArtistRepository;
import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.artist.exception.ArtistErrorCode.NOT_EXIST_ARTIST;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;

    public void registerFavoriteArtist(FavoriteArtistRequest request,
                                       LoginMember loginMember) {
        Long artistId = request.artistId();
        validateNotExistArtist(artistId);
        FavoriteArtist favoriteArtist = new FavoriteArtist(loginMember.id(), artistId);
        favoriteArtistRepository.save(favoriteArtist);
    }

    private void validateNotExistArtist(Long artistId) {
        if (!artistRepository.existsById(artistId)) {
            throw BusinessException.from(NOT_EXIST_ARTIST);
        }
    }
}
