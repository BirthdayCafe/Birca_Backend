SET foreign_key_checks = 0;

INSERT INTO birthday_cafe
(id, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant, twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (1, 1, 1, 1, 2, '2024-02-08T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000', 'IN_PROGRESS', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

SET foreign_key_checks = 1;
