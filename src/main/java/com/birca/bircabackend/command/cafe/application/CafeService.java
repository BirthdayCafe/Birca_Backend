package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.domain.CafeRepository;
import com.birca.bircabackend.command.cafe.domain.DayOff;
import com.birca.bircabackend.command.cafe.domain.DayOffRepository;
import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;
import com.birca.bircabackend.command.cafe.domain.value.CafeOption;
import com.birca.bircabackend.command.cafe.dto.CafeMenuRequest;
import com.birca.bircabackend.command.cafe.dto.CafeOptionRequest;
import com.birca.bircabackend.command.cafe.dto.CafeUpdateRequest;
import com.birca.bircabackend.command.cafe.dto.DayOffCreateRequest;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CafeService {

    private final DayOffRepository dayOffRepository;
    private final CafeRepository cafeRepository;
    private final EntityUtil entityUtil;

    public void update(LoginMember loginMember, CafeUpdateRequest request) {
        Long ownerId = loginMember.id();
        Cafe cafe = findCafe(ownerId);
        cafe.update(request.cafeName(), request.cafeAddress(),
                request.twitterAccount(), request.businessHours());
    }

    public void updateCafeMenus(LoginMember loginMember, List<CafeMenuRequest> requests) {
        Long ownerId = loginMember.id();
        Cafe cafe = findCafe(ownerId);
        cafe.updateCafeMenus(mapToCafeMenus(requests));
    }

    private List<CafeMenu> mapToCafeMenus(List<CafeMenuRequest> requests) {
        return requests.stream()
                .map(req -> new CafeMenu(req.name(), req.price()))
                .toList();
    }

    public void updateCafeOptions(LoginMember loginMember, List<CafeOptionRequest> requests) {
        Long ownerId = loginMember.id();
        Cafe cafe = findCafe(ownerId);
        cafe.updateCafeOptions(mapToCafeOptions(requests));
    }

    private List<CafeOption> mapToCafeOptions(List<CafeOptionRequest> request) {
        return request.stream()
                .map(req -> new CafeOption(req.name(), req.price()))
                .toList();
    }

    private Cafe findCafe(Long ownerId) {
        return cafeRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> BusinessException.from(CafeErrorCode.NOT_FOUND));
    }

    public void markDayOff(Long cafeId, LoginMember loginMember, DayOffCreateRequest request) {
        Cafe cafe = entityUtil.getEntity(Cafe.class, cafeId, CafeErrorCode.NOT_FOUND);
        List<LocalDateTime> savedDayOffs = dayOffRepository.findByCafeId(cafeId);
        List<LocalDateTime> requestedDayOffDates = request.datOffDates();
        deleteDayOff(cafeId, requestedDayOffDates, savedDayOffs);
        saveDayOff(loginMember, requestedDayOffDates, savedDayOffs, cafe);
    }

    private void deleteDayOff(Long cafeId, List<LocalDateTime> requestedDayOffDates, List<LocalDateTime> savedDayOffs) {
        for (LocalDateTime dayOffDate : savedDayOffs) {
            if (!requestedDayOffDates.contains(dayOffDate)) {
                dayOffRepository.deleteByCafeIdAndDayOffDate(cafeId, dayOffDate);
            }
        }
    }

    private void saveDayOff(LoginMember loginMember, List<LocalDateTime> requestedDayOffDates, List<LocalDateTime> savedDayOffs, Cafe cafe) {
        for (LocalDateTime dayOffDate : requestedDayOffDates) {
            if (!savedDayOffs.contains(dayOffDate)) {
                DayOff dayOff = cafe.markDayOff(loginMember.id(), dayOffDate);
                dayOffRepository.save(dayOff);
            }
        }
    }
}
