package com.birca.bircabackend.command.member.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Column;
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

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole role;

    public void changeRole(MemberRole role) {
        this.role = role;
    }

    public void registerNickname(String nickname) {
        this.nickname = nickname;
    }
}
