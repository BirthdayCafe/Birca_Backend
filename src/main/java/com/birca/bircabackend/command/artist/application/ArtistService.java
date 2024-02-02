package com.birca.bircabackend.command.artist.application;

import com.birca.bircabackend.command.artist.domain.*;
import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.command.artist.dto.InterestArtistRequest;
import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static com.birca.bircabackend.command.artist.exception.ArtistErrorCode.*;
import static com.birca.bircabackend.command.artist.exception.ArtistErrorCode.NOT_EXIST_ARTIST;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final FavoriteArtistRepository favoriteArtistRepository;
    private final InterestArtistRepository interestArtistRepository;
    private final InterestArtistBulkSaveRepository interestArtistBulkSaveRepository;

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
        List<Long> requestArtistIds = extractArtistIds(request);
        List<Long> registeredInterestArtistIds = getRegisteredArtistIds(fanId);

        validateArtistExceed(requestArtistIds.size(), registeredInterestArtistIds.size());
        validateDuplicateArtists(registeredInterestArtistIds, requestArtistIds);
        validateExistAllArtist(requestArtistIds);

        interestArtistBulkSaveRepository.saveAll(
                requestArtistIds.stream()
                        .map(artistId -> new InterestArtist(fanId, artistId))
                        .toList()
        );
    }

    private List<Long> getRegisteredArtistIds(Long fanId) {
        return interestArtistRepository.findByFanId(fanId)
                .stream()
                .map(InterestArtist::getArtistId)
                .toList();
    }

    private List<Long> extractArtistIds(List<InterestArtistRequest> request) {
        return request.stream()
                .map(InterestArtistRequest::artistId)
                .toList();
    }

    private void validateArtistExceed(int requestSize, int registeredSize) {
        if (requestSize + registeredSize > InterestArtist.REGISTER_LIMIT) {
            throw BusinessException.from(EXCEED_INTEREST_LIMIT);
        }
    }

    private void validateDuplicateArtists(List<Long> registeredInterestArtistIds,
                                          List<Long> requestArtistIds) {
        if (requestArtistIds.size() != new HashSet<>(requestArtistIds).size()) {
            throw BusinessException.from(DUPLICATE_INTEREST_ARTIST);
        }
        for (Long registeredInterestArtistId : registeredInterestArtistIds) {
            validateRegisteredIdInRequest(requestArtistIds, registeredInterestArtistId);
        }
    }

    private void validateRegisteredIdInRequest(List<Long> requestArtistIds, Long registeredInterestArtistId) {
        if (requestArtistIds.contains(registeredInterestArtistId)) {
            throw BusinessException.from(DUPLICATE_INTEREST_ARTIST);
        }
    }

    private void validateExistAllArtist(List<Long> artistIds) {
        if (artistRepository.countByIdIn(artistIds) != artistIds.size()) {
            throw BusinessException.from(NOT_EXIST_ARTIST);
        }
    }
}
