package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeService {

    private final BirthdayCafeRepository birthdayCafeRepository;

    public void applyRental(ApplyRentalRequest request, LoginMember loginMember) {
        Long hostId = loginMember.id();
        validateRentalPendingExists(hostId);
        BirthdayCafe birthdayCafe = request.toEntity(hostId);
        birthdayCafeRepository.save(birthdayCafe);
    }

    private void validateRentalPendingExists(Long hostId) {
        birthdayCafeRepository.findByHostIdAndProgressState(hostId, ProgressState.RENTAL_PENDING)
                .ifPresent(it -> {
                    throw BusinessException.from(BirthdayCafeErrorCode.RENTAL_PENDING_EXISTS);
                });
    }
}
