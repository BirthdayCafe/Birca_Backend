package com.birca.bircabackend.query.repository.model;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;

public record BirthdayCafeView(
        BirthdayCafe birthdayCafe,
        BirthdayCafeImage mainImage,
        Artist artist,
        ArtistGroup artistGroup
) {
}
