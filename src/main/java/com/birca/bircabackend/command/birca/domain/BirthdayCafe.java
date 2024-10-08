package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.common.domain.BaseEntity;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.*;

@Entity
@Table(indexes = @Index(name = "idx_start_end_date", columnList = "startDate, endDate"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class BirthdayCafe extends BaseEntity {

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
    @CollectionTable(name = "birthday_cafe_menu")
    private List<BirthdayCafeMenu> birthdayCafeMenus = new ArrayList<>();

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

    public static BirthdayCafe addBirthdayCafe(Long artistId, Long cafeId, Long cafeOwnerId, Schedule schedule,
                                               Visitants visitants, String twitterAccount, PhoneNumber hostPhoneNumber) {
        return BirthdayCafe.builder()
                .hostId(null)
                .artistId(artistId)
                .cafeId(cafeId)
                .cafeOwnerId(cafeOwnerId)
                .schedule(schedule)
                .visitants(visitants)
                .twitterAccount(twitterAccount)
                .hostPhoneNumber(hostPhoneNumber)
                .progressState(ProgressState.RENTAL_APPROVED)
                .visibility(Visibility.PRIVATE)
                .congestionState(CongestionState.SMOOTH)
                .specialGoodsStockState(SpecialGoodsStockState.ABUNDANT)
                .build();
    }

    public void cancelRental(Long memberId) {
        if (!progressState.isRentalPending() && !progressState.isRentalApproved()) {
            throw BusinessException.from(INVALID_CANCEL_RENTAL);
        }
        if (!(isHost(memberId) || isOwner(memberId))) {
            throw BusinessException.from(UNAUTHORIZED_CANCEL);
        }
        progressState = ProgressState.RENTAL_CANCELED;
    }

    public void approveRental(Long memberId) {
        if (!isOwner(memberId)) {
            throw BusinessException.from(UNAUTHORIZED_UPDATE);
        }
        progressState = ProgressState.RENTAL_APPROVED;
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

    public void changeState(Schedule schedule, LocalDate today, LocalDate yesterday) {
        LocalDate startDate = schedule.getStartDate().toLocalDate();
        LocalDate endDate = schedule.getEndDate().toLocalDate();
        changeStateForSameDate(schedule, today, yesterday, startDate, endDate);
        changeForDifferentDate(schedule, today, startDate, endDate);
    }

    private void changeForDifferentDate(Schedule schedule, LocalDate today, LocalDate startDate, LocalDate endDate) {
        if (!isSameDate(schedule)) {
            if (today.isEqual(startDate)) {
                this.progressState = ProgressState.IN_PROGRESS;
                this.visibility = Visibility.PUBLIC;
            }
            if (today.isEqual(endDate)) {
                this.progressState = ProgressState.FINISHED;
                this.visibility = Visibility.PRIVATE;
            }
        }
    }

    private void changeStateForSameDate(Schedule schedule, LocalDate today, LocalDate yesterday, LocalDate startDate, LocalDate endDate) {
        if (isSameDate(schedule)) {
            if (today.isEqual(startDate)) {
                this.progressState = ProgressState.IN_PROGRESS;
                this.visibility = Visibility.PUBLIC;
            }
            if (yesterday.isEqual(endDate)) {
                this.progressState = ProgressState.FINISHED;
                this.visibility = Visibility.PRIVATE;
            }
        }
    }

    private static boolean isSameDate(Schedule schedule) {
        return schedule.getStartDate().isEqual(schedule.getEndDate());
    }

    public void replaceSpecialGoods(Long memberId, List<SpecialGoods> specialGoods) {
        validateIsHost(memberId);
        if (!progressState.isInProgress() && !progressState.isRentalApproved()) {
            throw BusinessException.from(INVALID_UPDATE);
        }
        this.specialGoods = specialGoods;
    }

    public void replaceMenus(Long memberId, List<BirthdayCafeMenu> birthdayCafeMenus) {
        validateIsHost(memberId);
        if (!progressState.isInProgress() && !progressState.isRentalApproved()) {
            throw BusinessException.from(INVALID_UPDATE);
        }
        this.birthdayCafeMenus = birthdayCafeMenus;
    }

    public void replaceLuckyDraws(Long memberId, List<LuckyDraw> luckyDraws) {
        validateIsHost(memberId);
        if (!progressState.isInProgress() && !progressState.isRentalApproved()) {
            throw BusinessException.from(INVALID_UPDATE);
        }
        this.luckyDraws = luckyDraws;
    }

    public void updateMemo(Memo memo, Long cafeOwnerId, String content) {
        if (!isOwner(cafeOwnerId)) {
            throw BusinessException.from(INVALID_UPDATE);
        }
        memo.update(content);
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
