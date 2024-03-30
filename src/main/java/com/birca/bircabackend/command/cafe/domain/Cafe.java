package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.command.cafe.domain.value.CafeImage;
import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;
import com.birca.bircabackend.command.cafe.domain.value.CafeOption;
import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private String twitterAccount;

    @Column(nullable = false)
    private String businessHours;

    @ElementCollection
    @CollectionTable(name = "cafe_image")
    private List<CafeImage> cafeImages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cafe_menu")
    private List<CafeMenu> cafeMenus = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cafe_option")
    private List<CafeOption> cafeOptions = new ArrayList<>();
}
