package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeLike;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeLikeRepository;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeLikeService {

    private final BirthdayCafeLikeRepository birthdayCafeLikeRepository;
    private final EntityUtil entityUtil;

    public void like(Long birthdayCafeId, LoginMember loginMember) {
        Long visitantId = loginMember.id();
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, BirthdayCafeErrorCode.NOT_FOUND);
        BirthdayCafeLike birthdayCafeLike = birthdayCafe.like(visitantId);
        birthdayCafeLikeRepository.save(birthdayCafeLike);
    }

    public void cancelLike(Long birthdayCafeId, LoginMember loginMember) {
        birthdayCafeLikeRepository
                .findByVisitantIdAndBirthdayCafeId(loginMember.id(), birthdayCafeId)
                .ifPresentOrElse(
                        birthdayCafeLikeRepository::delete,
                        () -> {
                            throw BusinessException.from(BirthdayCafeErrorCode.CANNOT_CANCEL_LIKE);
                        }
                );
    }
}
