package com.birca.bircabackend.command.artist.application;

import com.birca.bircabackend.command.artist.domain.*;
import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.command.artist.dto.InterestArtistRequest;
import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.birca.bircabackend.command.artist.exception.ArtistErrorCode.NOT_EXIST_ARTIST;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;
    private final InterestArtistBulkSaveRepository interestArtistBulkSaveRepository;
    private final InterestArtistRegisterValidator interestArtistRegisterValidator;

    public void registerFavoriteArtist(FavoriteArtistRequest request,
                                       LoginMember loginMember) {
        Long artistId = request.artistId();
        Long fanId = loginMember.id();
        validateNotExistArtist(artistId);
        favoriteArtistRepository.findByFanId(fanId)
                .ifPresentOrElse(
                        favoriteArtist -> favoriteArtist.changeArtist(artistId),
                        () -> favoriteArtistRepository.save(new FavoriteArtist(fanId, artistId))
                );
    }

    private void validateNotExistArtist(Long artistId) {
        if (!artistRepository.existsById(artistId)) {
            throw BusinessException.from(NOT_EXIST_ARTIST);
        }
    }

    public void registerInterestArtist(List<InterestArtistRequest> request, LoginMember loginMember) {
        Long fanId = loginMember.id();
        List<InterestArtist> interestArtists = request.stream()
                .map(req -> new InterestArtist(fanId, req.artistId()))
                .toList();
        interestArtistRegisterValidator.validate(fanId, interestArtists);
        interestArtistBulkSaveRepository.saveAll(interestArtists);
    }
}
