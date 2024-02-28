package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.common.domain.BaseEntity;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.INVALID_CANCEL_RENTAL;
import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.UNAUTHORIZED_CANCEL;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class BirthdayCafe extends BaseEntity {

    @Column(nullable = false)
    private Long hostId;

    @Column(nullable = false)
    private Long artistId;

    @Column(nullable = false)
    private Long cafeId;

    @Column(nullable = false)
    private Long cafeOwnerId;

    @Embedded
    private Schedule schedule;

    @Embedded
    private Visitants visitants;

    private String twitterAccount;

    @Embedded
    private PhoneNumber hostPhoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgressState progressState;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CongestionState congestionState;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SpecialGoodsStockState specialGoodsStockState;

    public static BirthdayCafe applyRental(Long hostId,
                                           Long artistId,
                                           Long cafeId,
                                           Long cafeOwnerId,
                                           Schedule schedule,
                                           Visitants visitants,
                                           String twitterAccount,
                                           PhoneNumber hostPhoneNumber) {
        return new BirthdayCafe(
                hostId,
                artistId,
                cafeId,
                cafeOwnerId,
                schedule,
                visitants,
                twitterAccount,
                hostPhoneNumber,
                ProgressState.RENTAL_PENDING,
                Visibility.PRIVATE,
                CongestionState.SMOOTH,
                SpecialGoodsStockState.ABUNDANT
        );
    }

    public void cancelRental(Long memberId) {
        if (!progressState.isRentalPending()) {
            throw BusinessException.from(INVALID_CANCEL_RENTAL);
        }
        if (!isAuthorizedMember(memberId)) {
            throw BusinessException.from(UNAUTHORIZED_CANCEL);
        }
        progressState = ProgressState.RENTAL_CANCELED;
    }

    private boolean isAuthorizedMember(Long memberId) {
        return memberId.equals(hostId) || memberId.equals(cafeOwnerId);
    }
}
