package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.domain.value.PhoneNumber;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.birca.domain.value.Schedule;
import com.birca.bircabackend.command.birca.domain.value.Visitants;
import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
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
}
