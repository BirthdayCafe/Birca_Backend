package com.birca.bircabackend.command.artist.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "IDX_ARTIST_NAME", columnList = "name"))
@Getter
public class Artist extends BaseEntity {

    private Long groupId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Lob
    private String imageUrl;
}
