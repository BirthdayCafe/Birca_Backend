package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.repository.InterestArtistQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestArtistQueryService {

    private final InterestArtistQueryRepository interestArtistQueryRepository;

    public List<ArtistResponse> findInterestArtists(LoginMember loginMember) {
        return interestArtistQueryRepository.findInterestArtistsByFanId(loginMember.id())
                .stream()
                .map(ArtistResponse::new)
                .toList();
    }
}
