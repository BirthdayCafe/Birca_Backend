package com.birca.bircabackend.command.like.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.command.like.domain.*;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeLikeService {

    private final BirthdayCafeLikeRepository birthdayCafeLikeRepository;
    private final BirthdayCafeLikeValidator likeValidator;
    private final LikeRepository likeRepository;

    public void like(Long birthdayCafeId, LoginMember loginMember) {
        Long visitantId = loginMember.id();
        LikeTarget likeTarget = new LikeTarget(birthdayCafeId, LikeTargetType.BIRTHDAY_CAFE);
        Like like = Like.create(visitantId, likeTarget, likeValidator);
        likeRepository.save(like);
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
