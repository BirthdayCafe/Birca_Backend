package com.birca.bircabackend.command.like.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes", uniqueConstraints = {@UniqueConstraint(
        name = "uc_like_target", columnNames = {"targetId", "targetType"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Like extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Embedded
    private LikeTarget target;

    public static Like create(Long memberId, LikeTarget target, LikeValidator validator) {
        validator.validate(target);
        return new Like(memberId, target);
    }

    private Like(Long memberId, LikeTarget target) {
        this.memberId = memberId;
        this.target = target;
    }
}
