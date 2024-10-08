package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;
import com.birca.bircabackend.command.cafe.domain.value.CafeOption;
import com.birca.bircabackend.common.domain.BaseEntity;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.birca.bircabackend.command.cafe.exception.CafeErrorCode.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "uc_cafe_businesslicenseid",
        columnNames = {"businessLicenseId"}
)})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cafe extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long businessLicenseId;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String twitterAccount;

    private String businessHours;

    @ElementCollection
    @CollectionTable(name = "cafe_menu")
    private List<CafeMenu> cafeMenus = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cafe_option")
    private List<CafeOption> cafeOptions = new ArrayList<>();

    public Cafe(Long businessLicenseId, Long ownerId, String name, String address) {
        this.businessLicenseId = businessLicenseId;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
    }

    public void update(String name, String address, String twitterAccount, String businessHours) {
        this.name = name;
        this.address = address;
        this.twitterAccount = twitterAccount;
        this.businessHours = businessHours;
    }

    public void updateCafeMenus(List<CafeMenu> cafeMenus) {
        this.cafeMenus = cafeMenus;
    }

    public void updateCafeOptions(List<CafeOption> cafeOptions) {
        this.cafeOptions = cafeOptions;
    }

    public DayOff markDayOff(Long ownerId, LocalDateTime dayOffDate) {
        validateIsOwner(ownerId);
        return new DayOff(this.getId(), dayOffDate);
    }

    private void validateIsOwner(Long memberId) {
        if (!isOwner(memberId)) {
            throw BusinessException.from(UNAUTHORIZED_UPDATE);
        }
    }

    private boolean isOwner(Long memberId) {
        return memberId.equals(ownerId);
    }
}
