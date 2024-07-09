SET foreign_key_checks = 0;

INSERT INTO birthday_cafe (id, host_id, artist_id, cafe_id, cafe_owner_id,
                           start_date, end_date,
                           minimum_visitant, maximum_visitant,
                           twitter_account, host_phone_number,
                           progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (1, 2, 1, 1, 3,
        '2022-03-01 12:00:00', '2022-03-01 18:00:00',
        100, 200,
        'cafeTwitter', '010-1234-5678',
        'RENTAL_APPROVED', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

INSERT INTO memo (id, birthday_cafe_id, content)
VALUES (1, 1, '새로운 생일 카페 메모 내용');

SET foreign_key_checks = 1;
