package com.birca.bircabackend.command.member.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
}
