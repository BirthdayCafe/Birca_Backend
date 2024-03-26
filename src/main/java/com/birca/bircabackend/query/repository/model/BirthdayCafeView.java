package com.birca.bircabackend.query.repository.model;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.like.domain.Like;

public record BirthdayCafeView(
        BirthdayCafe birthdayCafe,
        BirthdayCafeImage mainImage,
        Artist artist,
        ArtistGroup artistGroup,
        Like like
) {

    public BirthdayCafeView(BirthdayCafe birthdayCafe, BirthdayCafeImage mainImage, Artist artist, ArtistGroup artistGroup) {
        this(birthdayCafe, mainImage, artist, artistGroup, null);
    }
}
