INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'VISITANT'),
       (2, '민혁', 'cm@gmail.com', '341234', 'kakao', 'HOST'),
       (3, '카페 사장', 'owner@gmail.com', '23412', 'kakao', 'OWNER');

INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code,
                             address, image_url, registration_approved)
VALUES (1, 3, '카페 사장', '스타벅스', '123', '12', '12345', '서울', 'business-license-imgae.com', false);

INSERT INTO cafe (id, owner_id, business_license_id, name, address, twitter_account, business_hours)
VALUES (1, 3, 1, '메가커피', '경기도 성남시 분당구 판교역로 235', '@ChaseM', '8시 - 22시');

-- 아티스트
INSERT INTO artist_group (id, name, image_url)
VALUES (1, '샤이니', 'group-image.com');

INSERT INTO artist (id, group_id, name, image_url)
VALUES (1, 1, '민호', 'image1.com'),
       (2, null, '아이유', 'image2.com');

-- 회원 1이 찜할 생일 카페
INSERT INTO birthday_cafe
(id, name, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant,
 twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (1, '민호 생일 카페', 1, 1, 1, 2, '2024-03-18T00:00:00', '2024-03-19T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'IN_PROGRESS', 'PRIVATE', 'SMOOTH', 'ABUNDANT'),
       (2, '아이유 생일 카페', 1, 2, 1, 2, '2024-03-20T00:00:00', '2024-03-23T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_PENDING', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

-- 회원 2가 찜할 생일 카페
INSERT INTO birthday_cafe
(id, name, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant,
 twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (3, '태민 생일 카페', 2, 1, 1, 2, '2024-03-20T00:00:00', '2024-03-21T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'IN_PROGRESS', 'PRIVATE', 'SMOOTH', 'ABUNDANT'),
       (4, '민호 생일 카페', 2, 2, 1, 2, '2024-03-01T00:00:00', '2024-03-03T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_PENDING', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

-- 일반 생일 카페
INSERT INTO birthday_cafe
(id, name, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant,
 twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (5, '태민 생일 카페', 2, 1, 1, 2, '2024-03-08T00:00:00', '2024-03-09T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'IN_PROGRESS', 'PRIVATE', 'SMOOTH', 'ABUNDANT'),
       (6, '아이유 생일 카페', 2, 2, 1, 2, '2024-03-23T00:00:00', '2024-03-24T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_PENDING', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

INSERT INTO birthday_cafe_image
(id, birthday_cafe_id, image_url, is_main)
VALUES (1, 1, 'image1.com', true),
       (2, 2, 'image2.com', true);

INSERT INTO likes
(id, target_id, visitant_id, target_type)
VALUES (1, 1, 1, 'BIRTHDAY_CAFE'),
       (2, 2, 1, 'BIRTHDAY_CAFE'),
       (3, 3, 2, 'BIRTHDAY_CAFE'),
       (4, 4, 2, 'BIRTHDAY_CAFE');
