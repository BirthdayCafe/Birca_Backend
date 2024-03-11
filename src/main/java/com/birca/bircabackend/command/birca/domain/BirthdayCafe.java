package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.common.domain.BaseEntity;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "special_goods")
    private List<SpecialGoods> specialGoods = new ArrayList<>();

    public static BirthdayCafe applyRental(Long hostId, Long artistId, Long cafeId, Long cafeOwnerId, Schedule schedule,
                                           Visitants visitants, String twitterAccount, PhoneNumber hostPhoneNumber) {
        return BirthdayCafe.builder()
                .hostId(hostId)
                .artistId(artistId)
                .cafeId(cafeId)
                .cafeOwnerId(cafeOwnerId)
                .schedule(schedule)
                .visitants(visitants)
                .twitterAccount(twitterAccount)
                .hostPhoneNumber(hostPhoneNumber)
                .progressState(ProgressState.RENTAL_PENDING)
                .visibility(Visibility.PRIVATE)
                .congestionState(CongestionState.SMOOTH)
                .specialGoodsStockState(SpecialGoodsStockState.ABUNDANT)
                .build();
    }

    public void cancelRental(Long memberId) {
        if (!progressState.isRentalPending()) {
            throw BusinessException.from(INVALID_CANCEL_RENTAL);
        }
        if (!(isHost(memberId) || isOwner(memberId))) {
            throw BusinessException.from(UNAUTHORIZED_CANCEL);
        }
        progressState = ProgressState.RENTAL_CANCELED;
    }

    public void changeState(SpecialGoodsStockState state, Long memberId) {
        validateIsHost(memberId);
        validateIsInProgressState();
        this.specialGoodsStockState = state;
    }

    public void changeState(CongestionState state, Long memberId) {
        validateIsHost(memberId);
        validateIsInProgressState();
        this.congestionState = state;
    }

    public void changeState(Visibility visibility, Long memberId) {
        validateIsHost(memberId);
        if (progressState.isRentalPending()) {
            throw BusinessException.from(INVALID_UPDATE);
        }
        this.visibility = visibility;
    }

    public BirthdayCafeLike like(Long visitantId) {
        if (progressState.isRentalPending() || progressState.isRentalCanceled()) {
            throw BusinessException.from(INVALID_LIKE_REQUEST);
        }
        return new BirthdayCafeLike(this.getId(), visitantId);
    }

    public void registerSpecialGoods(Long memberId, List<SpecialGoods> specialGoods) {
        validateIsHost(memberId);
        if (!progressState.isInProgress() && !progressState.isRentalApproved()) {
            throw BusinessException.from(INVALID_UPDATE);
        }
        this.specialGoods = specialGoods;
    }

    private void validateIsHost(Long memberId) {
        if (!isHost(memberId)) {
            throw BusinessException.from(UNAUTHORIZED_UPDATE);
        }
    }

    private void validateIsInProgressState() {
        if (!progressState.isInProgress()) {
            throw BusinessException.from(INVALID_UPDATE);
        }
    }

    private boolean isOwner(Long memberId) {
        return memberId.equals(cafeOwnerId);
    }

    private boolean isHost(Long memberId) {
        return memberId.equals(hostId);
    }
}
