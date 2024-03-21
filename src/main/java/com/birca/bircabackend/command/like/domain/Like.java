package com.birca.bircabackend.command.like.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes", uniqueConstraints = {@UniqueConstraint(
        name = "uc_like_target", columnNames = {"targetId", "targetType"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Like extends BaseEntity {

    @Embedded
    private LikeTarget target;
}
