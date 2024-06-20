package com.birca.bircabackend.command.member.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "UC_IDENTITY_KEY",
        columnNames = {"socialId", "socialProvider"}
)})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Member extends BaseEntity {

    @Embedded
    private Nickname nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole role;

    @Column(nullable = false)
    @Embedded
    private IdentityKey identityKey;

    public static Member join(String email, IdentityKey identityKey) {
        return new Member(null, email, MemberRole.NOTHING, identityKey);
    }

    public void changeRole(MemberRole role) {
        this.role = role;
    }

    public void registerNickname(Nickname nickname) {
        this.nickname = nickname;
    }
}
