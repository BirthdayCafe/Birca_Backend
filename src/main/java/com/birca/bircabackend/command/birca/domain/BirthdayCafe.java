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

    private String name;

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

    @ElementCollection
    @CollectionTable(name = "special_goods")
    private List<SpecialGoods> specialGoods = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "menu")
    private List<Menu> menus = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "lucky_draw")
    private List<LuckyDraw> luckyDraws = new ArrayList<>();

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

    public void updateName(Long memberId, String name) {
        validateIsHost(memberId);
        this.name = name;
    }

    public void updateTwitterAccount(Long memberId, String twitterAccount) {
        validateIsHost(memberId);
        this.twitterAccount = twitterAccount;
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

    public void replaceSpecialGoods(Long memberId, List<SpecialGoods> specialGoods) {
        validateIsHost(memberId);
        if (!progressState.isInProgress() && !progressState.isRentalApproved()) {
            throw BusinessException.from(INVALID_UPDATE);
        }
        this.specialGoods = specialGoods;
    }

    public void replaceMenus(Long memberId, List<Menu> menus) {
        validateIsHost(memberId);
        if (!progressState.isInProgress() && !progressState.isRentalApproved()) {
            throw BusinessException.from(INVALID_UPDATE);
        }
        this.menus = menus;
    }

    public void replaceLuckyDraws(Long memberId, List<LuckyDraw> luckyDraws) {
        validateIsHost(memberId);
        if (!progressState.isInProgress() && !progressState.isRentalApproved()) {
            throw BusinessException.from(INVALID_UPDATE);
        }
        this.luckyDraws = luckyDraws;
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
