package com.birca.bircabackend.command.artist.domain;

import java.util.List;

public interface InterestArtistBulkSaveRepository {

    void saveAll(List<InterestArtist> interestArtists);
}
