package com.birca.bircabackend.query.repository.model;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.command.member.domain.Member;

public record BirthdayCafeView(
        BirthdayCafe birthdayCafe,
        BirthdayCafeImage mainImage,
        Artist artist,
        ArtistGroup artistGroup,
        Like like,
        Cafe cafe,
        Member member
) {

    public BirthdayCafeView(BirthdayCafe birthdayCafe, BirthdayCafeImage mainImage, Artist artist, ArtistGroup artistGroup) {
        this(birthdayCafe, mainImage, artist, artistGroup, null, null, null);
    }

    public BirthdayCafeView(BirthdayCafe birthdayCafe, BirthdayCafeImage mainImage, Artist artist, ArtistGroup artistGroup, Like like, Cafe cafe) {
        this(birthdayCafe, mainImage, artist, artistGroup, like, cafe, null);
    }

    public BirthdayCafeView(BirthdayCafe birthdayCafe, Artist artist, ArtistGroup artistGroup, Member member) {
        this(birthdayCafe, null, artist, artistGroup, null, null, member);
    }
}
