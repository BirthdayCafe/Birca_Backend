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

import static com.birca.bircabackend.command.like.exception.LikeErrorCode.INVALID_CANCEL;

@Service
@Transactional
@RequiredArgsConstructor
public class CafeLikeService {

    private final LikeRepository likeRepository;
    private final CafeLikeValidator likeValidator;

    public void like(Long cafeId, LoginMember loginMember) {
        LikeTarget likeTarget = new LikeTarget(cafeId, LikeTargetType.CAFE);
        Like like = Like.create(loginMember.id(), likeTarget, likeValidator);
        likeRepository.save(like);
    }

    public void cancelLike(Long cafeId, LoginMember loginMember) {
        LikeTarget likeTarget = new LikeTarget(cafeId, LikeTargetType.CAFE);
        likeRepository.findByVisitantIdAndTarget(loginMember.id(), likeTarget)
                .ifPresentOrElse(
                        likeRepository::delete,
                        () -> {
                            throw BusinessException.from(INVALID_CANCEL);
                        }
                );
    }
}
