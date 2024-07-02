package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.domain.value.Schedule;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.query.dto.*;
import com.birca.bircabackend.query.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CafeQueryService {

    private final LikedCafeQueryRepository likedCafeQueryRepository;
    private final BirthdayCafeQueryRepository birthdayCafeQueryRepository;
    private final CafeImageQueryRepository cafeImageRepository;
    private final DayOffQueryRepository dayOffQueryRepository;
    private final CafeQueryRepository cafeQueryRepository;
    private final EntityUtil entityUtil;

    public MyCafeDetailResponse findMyCafeDetails(LoginMember loginMember, DateParams dateParams) {
        Cafe cafe = cafeQueryRepository.findByOwnerId(loginMember.id())
                .orElseThrow(() -> BusinessException.from(CafeErrorCode.NOT_FOUND));
        Long cafeId = cafe.getId();
        List<String> cafeImages = cafeImageRepository.findByCafeId(cafeId);
        Integer year = dateParams.getYear();
        Integer month = dateParams.getMonth();
        List<LocalDateTime> dayOffDates = dayOffQueryRepository.findDayOffDateByCafeId(cafeId, year, month);
        return MyCafeDetailResponse.of(cafe, cafeImages, dayOffDates);
    }

    public List<CafeSearchResponse> searchRentalAvailableCafes(LoginMember loginMember, CafeParams cafeParams, PagingParams pagingParams) {
        return cafeQueryRepository.searchRentalAvailableCafes(loginMember, cafeParams, pagingParams)
                .stream()
                .map(CafeSearchResponse::from)
                .toList();
    }

    public CafeDetailResponse findCafeDetail(LoginMember loginMember, Long cafeId) {
        Boolean liked = likedCafeQueryRepository.existsByVisitantIdAndTargetIsCafe(loginMember.id(), cafeId);
        Cafe cafe = entityUtil.getEntity(Cafe.class, cafeId, CafeErrorCode.NOT_FOUND);
        List<String> cafeImages = cafeImageRepository.findByCafeId(cafeId);
        return CafeDetailResponse.from(liked, cafe, cafeImages);
    }

    public List<CafeRentalDateResponse> findCafeRentalDates(Long cafeId, DateParams dateParams) {
        entityUtil.getEntity(Cafe.class, cafeId, CafeErrorCode.NOT_FOUND);
        Integer year = dateParams.getYear();
        Integer month = dateParams.getMonth();
        List<LocalDateTime> dayOffDates = dayOffQueryRepository.findDayOffDateByCafeId(cafeId, year, month);
        List<Schedule> schedules = birthdayCafeQueryRepository.findScheduleByCafeId(cafeId, year, month);
        return CafeRentalDateResponse.from(schedules, dayOffDates);
    }
}
