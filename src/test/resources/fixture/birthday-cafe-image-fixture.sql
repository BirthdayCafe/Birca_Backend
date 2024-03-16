SET foreign_key_checks = 0;

INSERT INTO birthday_cafe
(id, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant, twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (1, 1, 1, 1, 2, '2024-02-08T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000', 'IN_PROGRESS', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

INSERT INTO birthday_cafe
(id, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant, twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (2, 1, 1, 1, 2, '2024-04-08T00:00:00', '2024-04-10T00:00:00', 50, 100, '@ChaseM', '010-0000-0000', 'IN_PROGRESS', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

INSERT INTO birthday_cafe_image
(id, birthday_cafe_id, image_url, is_main)
VALUES (1, 1, 'image1.com', false),
       (2, 1, 'image2.com', false),
       (3, 1, 'image3.com', false),
       (4, 1, 'image4.com', false),
       (5, 1, 'image5.com', false),
       (6, 1, 'image6.com', false),
       (7, 1, 'image7.com', false),
       (8, 1, 'image8.com', false),
       (9, 1, 'image9.com', false),
       (10, 1, 'image10.com', false);

INSERT INTO birthday_cafe_image
(id, birthday_cafe_id, image_url, is_main)
VALUES (11, 2, 'image1.com', false),
       (12, 2, 'image2.com', false),
       (13, 2, 'image3.com', false);

SET foreign_key_checks = 1;
