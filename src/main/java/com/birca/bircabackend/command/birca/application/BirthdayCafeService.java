package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
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
        BirthdayCafe birthdayCafe = request.toEntity(hostId);
        birthdayCafeRepository.save(birthdayCafe);
    }

    private void validateRentalPendingExists(Long hostId) {
        birthdayCafeRepository.findByHostIdAndProgressState(hostId, ProgressState.RENTAL_PENDING)
                .ifPresent(it -> {
                    throw BusinessException.from(RENTAL_PENDING_EXISTS);
                });
    }

    public void cancelRental(Long birthdayCafeId, LoginMember loginMember) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        validateAuthority(loginMember, birthdayCafe);
        birthdayCafe.cancelRental();
    }

    private void validateAuthority(LoginMember loginMember, BirthdayCafe birthdayCafe) {
        boolean isHost = birthdayCafe.isHost(loginMember.id());
        boolean isOwner = birthdayCafeRepository.isOwner(birthdayCafe, loginMember.id());
        if (!isHost && !isOwner) {
            throw BusinessException.from(UNAUTHORIZED_HOST);
        }
    }
}
