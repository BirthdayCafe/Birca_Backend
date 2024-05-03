SET foreign_key_checks = 0;

INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'VISITANT');

INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code,
                             address, image_url, registration_approved)
VALUES (1, 1, '카페 사장', '스타벅스', '123', '12', '12345', '서울', 'business-license-imgae.com', true),
       (2, 1, '카페 사장', '메가커피', '321', '21', '54321', '서울', 'business-license-imgae.com', false);

INSERT INTO cafe (id, owner_id, business_license_id, name, address, twitter_account, business_hours)
VALUES (1, 1, 1, '미스티우드', '경기도 시흥시 은계중앙로 115', '@ChaseM', '6시 - 22시'),
       (2, 2, 2, '우지커피', '경기도 성남시 분당구 판교역로 235', '@ChaseM', '8시 - 22시'),
       (3, 3, 3, '메가커피', '서울특별시 강남구 테헤란로 212', '@ChaseM', '9시 - 22시');

INSERT INTO birthday_cafe (id, host_id, artist_id, cafe_id, cafe_owner_id,
                           start_date, end_date,
                           minimum_visitant, maximum_visitant,
                           twitter_account, host_phone_number,
                           progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (1, 2, 1, 1, 3,
        '2024-02-15T00:00:00', '2024-02-16T00:00:00',
        100, 200,
        'cafeTwitter', '010-1234-5678',
        'RENTAL_APPROVED', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

INSERT INTO cafe_image (id, cafe_id, image_url)
VALUES (1, 1, 'image1.com'),
       (2, 1, 'image2.com'),
       (3, 1, 'image3.com'),
       (4, 1, 'image4.com'),
       (5, 1, 'image5.com'),
       (6, 2, 'image6.com'),
       (7, 3, 'image7.com');

INSERT INTO likes
(id, target_id, visitant_id, target_type)
VALUES (1, 2, 1, 'CAFE'),
       (2, 3, 1, 'CAFE');

INSERT INTO cafe_menu(cafe_id, name, price)
VALUES (1, '아메리카노', 1500);

INSERT INTO cafe_option(cafe_id, name, price)
VALUES (1, '액자', 2000);

SET foreign_key_checks = 1;
