package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.query.dto.CafeDetailResponse;
import com.birca.bircabackend.query.dto.CafeResponse;
import com.birca.bircabackend.query.repository.CafeImageRepository;
import com.birca.bircabackend.query.repository.CafeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CafeQueryService {

    private final CafeQueryRepository cafeQueryRepository;
    private final CafeImageRepository cafeImageRepository;

    public List<CafeResponse> findCafes(String name) {
        return cafeQueryRepository.findByName(name);
    }

    public CafeDetailResponse findCafeDetail(LoginMember loginMember) {
        Cafe cafe = cafeQueryRepository.findByOwnerId(loginMember.id())
                .orElseThrow(() -> BusinessException.from(CafeErrorCode.NOT_FOUND));
        List<String> cafeImages = cafeImageRepository.findByCafeId(cafe.getId());
        return CafeDetailResponse.of(cafe, cafeImages);
    }
}
