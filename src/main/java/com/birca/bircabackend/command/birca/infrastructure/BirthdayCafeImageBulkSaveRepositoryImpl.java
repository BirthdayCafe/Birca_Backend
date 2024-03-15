package com.birca.bircabackend.command.birca.infrastructure;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImageBulkSaveRepository;
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
public class BirthdayCafeImageBulkSaveRepositoryImpl implements BirthdayCafeImageBulkSaveRepository {

    private static final String INSERT_SQL =
            "INSERT INTO birthday_cafe_image(birthday_cafe_id, image_url, is_main) " +
            "values(:birthday_cafe_id, :image_url, :is_main)";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<BirthdayCafeImage> birthdayCafeImages) {
        jdbcTemplate.batchUpdate(INSERT_SQL, generateParameterSource(birthdayCafeImages));
    }

    private SqlParameterSource[] generateParameterSource(List<BirthdayCafeImage> birthdayCafeImages) {
        return birthdayCafeImages.stream()
                .map(birthdayCafeImage -> new MapSqlParameterSource(generateParams(birthdayCafeImage)))
                .toArray(SqlParameterSource[]::new);
    }

    private Map<String, Object> generateParams(BirthdayCafeImage birthdayCafeImage) {
        Map<String, Object> params = new HashMap<>();
        params.put("birthday_cafe_id", birthdayCafeImage.getBirthdayCafeId());
        params.put("image_url", birthdayCafeImage.getImageUrl());
        params.put("is_main", birthdayCafeImage.getIsMain());
        return params;
    }
}
