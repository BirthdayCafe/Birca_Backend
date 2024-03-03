package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import com.birca.bircabackend.command.birca.dto.StateChangeRequest;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeService {

    private final BirthdayCafeRepository birthdayCafeRepository;
    private final EntityUtil entityUtil;

    public void applyRental(ApplyRentalRequest request, LoginMember loginMember) {
        Long hostId = loginMember.id();
        validateRentalPendingExists(hostId);
        BirthdayCafe birthdayCafe = mapToBirthdayCafe(request, hostId);
        birthdayCafeRepository.save(birthdayCafe);
    }

    private BirthdayCafe mapToBirthdayCafe(ApplyRentalRequest request, Long hostId) {
        return BirthdayCafe.applyRental(
                hostId,
                request.artistId(),
                request.cafeId(),
                birthdayCafeRepository.findOwnerIdByCafeId(request.cafeId()),
                Schedule.of(request.startDate(), request.endDate()),
                Visitants.of(request.minimumVisitant(), request.maximumVisitant()),
                request.twitterAccount(),
                PhoneNumber.from(request.hostPhoneNumber())
        );
    }

    private void validateRentalPendingExists(Long hostId) {
        if (birthdayCafeRepository.existsByHostIdAndProgressState(hostId, ProgressState.RENTAL_PENDING)) {
            throw BusinessException.from(RENTAL_PENDING_EXISTS);
        }
    }

    public void cancelRental(Long birthdayCafeId, LoginMember loginMember) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        birthdayCafe.cancelRental(loginMember.id());
    }

    public void changeSpecialGoodsStockState(Long birthdayCafeId, LoginMember loginMember, StateChangeRequest request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        SpecialGoodsStockState state = SpecialGoodsStockState.valueOf(request.state());
        birthdayCafe.changeState(state, loginMember.id());
    }

    public void changeCongestionState(Long birthdayCafeId, LoginMember loginMember, StateChangeRequest request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        CongestionState state = CongestionState.valueOf(request.state());
        birthdayCafe.changeState(state, loginMember.id());
    }

    public void changeVisibility(Long birthdayCafeId, LoginMember loginMember, StateChangeRequest request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        Visibility state = Visibility.valueOf(request.state());
        birthdayCafe.changeState(state, loginMember.id());
    }
}
