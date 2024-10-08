package com.birca.bircabackend.command.artist.domain;

import com.birca.bircabackend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = @Index(name = "IDX_NAME", columnList = "name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArtistGroup extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Lob
    private String imageUrl;
}
