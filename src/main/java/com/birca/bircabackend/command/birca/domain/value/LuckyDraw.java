package com.birca.bircabackend.command.birca.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class LuckyDraw {

    @Column(nullable = false)
    private Integer rank;

    @Column(nullable = false)
    private String prize;

    public LuckyDraw(Integer rank, String prize) {
        this.rank = rank;
        this.prize = prize;
    }
}
