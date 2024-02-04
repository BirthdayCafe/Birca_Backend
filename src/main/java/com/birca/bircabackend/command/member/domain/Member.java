package com.birca.bircabackend.command.member.domain;

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
public class Member extends BaseEntity {

    @Embedded
    private Nickname nickname;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole role;

    public static Member join(String email) {
        return new Member(null, email, MemberRole.VISITANT);
    }

    public void changeRole(MemberRole role) {
        this.role = role;
    }

    public void registerNickname(Nickname nickname) {
        this.nickname = nickname;
    }
}
