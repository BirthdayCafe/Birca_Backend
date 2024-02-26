package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BirthdayCafe extends BaseEntity {

    @Column(nullable = false)
    private Long hostId;

    @Column(nullable = false)
    private Long artistId;

    @Column(nullable = false)
    private Long cafeId;

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
                                           Schedule schedule,
                                           Visitants visitants,
                                           String twitterAccount,
                                           PhoneNumber hostPhoneNumber) {
        return new BirthdayCafe(
                hostId,
                artistId,
                cafeId,
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
}
