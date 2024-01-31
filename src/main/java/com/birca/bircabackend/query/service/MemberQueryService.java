package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.member.domain.Nickname;
import com.birca.bircabackend.query.dto.NicknameCheckResponse;
import com.birca.bircabackend.query.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;

    public NicknameCheckResponse checkNickname(String nickname) {
        Boolean isDuplicated = memberQueryRepository.existsByNickname(new Nickname(nickname));
        return new NicknameCheckResponse(isDuplicated);
    }
}
