package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.repository.InterestArtistQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.birca.bircabackend.command.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestArtistQueryService {

    private final InterestArtistQueryRepository interestArtistQueryRepository;
    private final EntityUtil entityUtil;

    public List<ArtistResponse> findInterestArtists(LoginMember loginMember) {
        Member member = entityUtil.getEntity(Member.class, loginMember.id(), MEMBER_NOT_FOUND);
        return interestArtistQueryRepository.findInterestArtistsByFanId(member.getId())
                .stream()
                .map(ArtistResponse::new)
                .toList();
    }
}
