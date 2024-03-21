package com.birca.bircabackend.command.like.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.command.like.domain.LikeRepository;
import com.birca.bircabackend.command.like.domain.LikeTarget;
import com.birca.bircabackend.command.like.domain.LikeTargetType;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.like.exception.LikeErrorCode.INVALID_BIRTHDAY_CAFE_CANCEL;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeLikeService {

    private final BirthdayCafeLikeValidator likeValidator;
    private final LikeRepository likeRepository;

    public void like(Long birthdayCafeId, LoginMember loginMember) {
        Long visitantId = loginMember.id();
        LikeTarget likeTarget = new LikeTarget(birthdayCafeId, LikeTargetType.BIRTHDAY_CAFE);
        Like like = Like.create(visitantId, likeTarget, likeValidator);
        likeRepository.save(like);
    }

    public void cancelLike(Long birthdayCafeId, LoginMember loginMember) {
        LikeTarget likeTarget = new LikeTarget(birthdayCafeId, LikeTargetType.BIRTHDAY_CAFE);
        likeRepository.findByVisitantIdAndTarget(loginMember.id(), likeTarget)
                .ifPresentOrElse(
                        likeRepository::delete,
                        () -> {
                            throw BusinessException.from(INVALID_BIRTHDAY_CAFE_CANCEL);
                        }
                );
    }
}
