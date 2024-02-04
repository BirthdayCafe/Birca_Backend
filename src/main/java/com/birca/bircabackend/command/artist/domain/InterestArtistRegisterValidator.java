package com.birca.bircabackend.command.artist.domain;

import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static com.birca.bircabackend.command.artist.exception.ArtistErrorCode.*;

@Service
@RequiredArgsConstructor
public class InterestArtistRegisterValidator {

    private final ArtistRepository artistRepository;
    private final InterestArtistRepository interestArtistRepository;

    public void validate(Long fanId, List<InterestArtist> interestArtists) {
        List<Long> registeredArtistIds = extractArtistIds(interestArtistRepository.findByFanId(fanId));
        List<Long> newInterestArtistIds = extractArtistIds(interestArtists);

        validateExceedArtists(newInterestArtistIds.size(), registeredArtistIds.size());
        validateDuplicateArtists(newInterestArtistIds, registeredArtistIds);
        validateExistArtists(newInterestArtistIds);
    }

    private List<Long> extractArtistIds(List<InterestArtist> registered) {
        return registered.stream()
                .map(InterestArtist::getArtistId)
                .toList();
    }

    private void validateExceedArtists(int requestSize, int registeredSize) {
        if (requestSize + registeredSize > InterestArtist.REGISTER_LIMIT) {
            throw BusinessException.from(EXCEED_INTEREST_LIMIT);
        }
    }

    private void validateDuplicateArtists(List<Long> newArtistIds, List<Long> registeredInterestArtistIds) {
        if (newArtistIds.size() != new HashSet<>(newArtistIds).size()) {
            throw BusinessException.from(DUPLICATE_INTEREST_ARTIST);
        }
        for (Long registeredInterestArtistId : registeredInterestArtistIds) {
            validateRegisteredIdInNew(newArtistIds, registeredInterestArtistId);
        }
    }

    private void validateRegisteredIdInNew(List<Long> newArtistIds, Long registeredInterestArtistId) {
        if (newArtistIds.contains(registeredInterestArtistId)) {
            throw BusinessException.from(DUPLICATE_INTEREST_ARTIST);
        }
    }

    private void validateExistArtists(List<Long> artistIds) {
        if (artistRepository.countByIdIn(artistIds) != artistIds.size()) {
            throw BusinessException.from(NOT_EXIST_ARTIST);
        }
    }
}
