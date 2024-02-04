package com.birca.bircabackend.command.member.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "uc_member_email_registration_id",
        columnNames = {"email", "registrationId"}
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
    @Column(name = "member_role")
    private MemberRole role;

    @Column(nullable = false)
    private String registrationId;

    public static Member join(String email, String registrationId) {
        return new Member(null, email, MemberRole.VISITANT, registrationId);
    }

    public void changeRole(MemberRole role) {
        this.role = role;
    }

    public void registerNickname(Nickname nickname) {
        this.nickname = nickname;
    }
}
