package com.birca.bircabackend.command.artist.infrastructure;

import com.birca.bircabackend.command.artist.domain.InterestArtist;
import com.birca.bircabackend.command.artist.domain.InterestArtistBulkSaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class InterestArtistBulkSaveRepositoryImpl implements InterestArtistBulkSaveRepository {

    private static final String INSERT_SQL =
            "INSERT INTO interest_artist(fan_id, artist_id) values(:fan_id, :artist_id)";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<InterestArtist> interestArtists) {
        jdbcTemplate.batchUpdate(INSERT_SQL, generateParameterSource(interestArtists));
    }

    private SqlParameterSource[] generateParameterSource(List<InterestArtist> interestArtists) {
        return interestArtists.stream()
                .map(artist -> new MapSqlParameterSource(generateParams(artist)))
                .toArray(SqlParameterSource[]::new);
    }

    private Map<String, Object> generateParams(InterestArtist interestArtist) {
        Map<String, Object> params = new HashMap<>();
        params.put("fan_id", interestArtist.getFanId());
        params.put("artist_id", interestArtist.getArtistId());
        return params;
    }
}
