package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeLike;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeLikeRepository;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeLikeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeLikeErrorCode.ALREADY_LIKED;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeLikeService {

    private final BirthdayCafeLikeRepository birthdayCafeLikeRepository;
    private final EntityUtil entityUtil;

    public void like(Long birthdayCafeId, LoginMember loginMember) {
        Long visitantId = loginMember.id();
        validateBirthdayCafeLikeExists(birthdayCafeId, visitantId);
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        checkLikeAvailability(birthdayCafe);
        BirthdayCafeLike birthdayCafeLike = new BirthdayCafeLike(visitantId, birthdayCafeId);
        birthdayCafeLikeRepository.save(birthdayCafeLike);
    }

    private void validateBirthdayCafeLikeExists(Long birthdayCafeId, Long visitantId) {
        if (birthdayCafeLikeRepository.existsByVisitantIdAndBirthdayCafeId(birthdayCafeId, visitantId)) {
            throw BusinessException.from(ALREADY_LIKED);
        }
    }

    private static void checkLikeAvailability(BirthdayCafe birthdayCafe) {
        ProgressState progressState = birthdayCafe.getProgressState();
        if (progressState.isRentalPending() || progressState.isRentalCanceled()) {
            throw BusinessException.from(BirthdayCafeLikeErrorCode.INVALID_LIKE_REQUEST);
        }
    }

    public void cancelLike(Long birthdayCafeId, LoginMember loginMember) {
        birthdayCafeLikeRepository
                .findByVisitantIdAndBirthdayCafeId(loginMember.id(), birthdayCafeId)
                .ifPresentOrElse(
                        birthdayCafeLikeRepository::delete,
                        () -> {
                            throw BusinessException.from(BirthdayCafeLikeErrorCode.NOT_FOUND);
                        }
                );
    }
}
