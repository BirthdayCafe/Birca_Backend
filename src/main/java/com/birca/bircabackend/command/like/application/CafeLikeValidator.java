package com.birca.bircabackend.command.like.application;

import com.birca.bircabackend.command.cafe.domain.BusinessLicenseRepository;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.command.like.domain.LikeTarget;
import com.birca.bircabackend.command.like.domain.LikeValidator;
import com.birca.bircabackend.command.like.exception.LikeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.BUSINESS_LICENSE_NOT_FOUND;
import static com.birca.bircabackend.command.cafe.exception.CafeErrorCode.NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CafeLikeValidator implements LikeValidator {

    private final EntityUtil entityUtil;
    private final BusinessLicenseRepository businessLicenseRepository;

    @Override
    public void validate(Like like) {
        LikeTarget target = like.getTarget();
        Cafe cafe = entityUtil.getEntity(Cafe.class, target.getTargetId(), NOT_FOUND);
        if (!businessLicenseRepository.existsByRegistrationApprovedAndOwnerId(cafe.getOwnerId())) {
            throw BusinessException.from(LikeErrorCode.INVALID_CAFE_LIKE);
        }
    }
}
