package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.ArtistGroup;

public record ArtistGroupResponse(
        Long groupId,
        String groupName,
        String groupImage) {

    public ArtistGroupResponse(ArtistGroup group) {
        this(group.getId(), group.getName(), group.getImageUrl());
    }
}
