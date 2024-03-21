package com.birca.bircabackend.command.like.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.like.domain.LikeTarget;
import com.birca.bircabackend.command.like.domain.LikeValidator;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.INVALID_LIKE_REQUEST;
import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.NOT_FOUND;

@Component
@RequiredArgsConstructor
public class BirthdayCafeLikeValidator implements LikeValidator {

    private final EntityUtil entityUtil;

    @Override
    public void validate(LikeTarget target) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, target.getTargetId(), NOT_FOUND);
        ProgressState progressState = birthdayCafe.getProgressState();
        if (progressState.isRentalPending() || progressState.isRentalCanceled()) {
            throw BusinessException.from(INVALID_LIKE_REQUEST);
        }
    }
}
