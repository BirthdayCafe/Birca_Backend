package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.query.dto.*;
import com.birca.bircabackend.query.repository.CafeImageQueryRepository;
import com.birca.bircabackend.query.repository.CafeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CafeQueryService {

    private final BirthdayCafeRepository birthdayCafeRepository;
    private final CafeImageQueryRepository cafeImageRepository;
    private final CafeQueryRepository cafeQueryRepository;
    private final EntityUtil entityUtil;

    public List<CafeResponse> findCafes(String name) {
        return cafeQueryRepository.findByName(name);
    }

    public MyCafeDetailResponse findMyCafeDetails(LoginMember loginMember) {
        Cafe cafe = cafeQueryRepository.findByOwnerId(loginMember.id())
                .orElseThrow(() -> BusinessException.from(CafeErrorCode.NOT_FOUND));
        List<String> cafeImages = cafeImageRepository.findByCafeId(cafe.getId());
        return MyCafeDetailResponse.of(cafe, cafeImages);
    }

    public List<CafeSearchResponse> searchCafes(LoginMember loginMember, CafeParams cafeParams, PagingParams pagingParams) {
        return cafeQueryRepository.searchCafes(loginMember, cafeParams, pagingParams)
                .stream()
                .map(CafeSearchResponse::from)
                .toList();
    }

    public CafeDetailResponse findCafeDetail(Long cafeId) {
        Cafe cafe = entityUtil.getEntity(Cafe.class, cafeId, CafeErrorCode.NOT_FOUND);
        List<BirthdayCafe> birthdayCafes = birthdayCafeRepository.findByCafeId(cafeId);
        return CafeDetailResponse.from(cafe, birthdayCafes);
    }
}
