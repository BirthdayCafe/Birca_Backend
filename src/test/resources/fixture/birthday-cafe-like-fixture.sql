-- 찜하기를 누를 회원
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'VISITANT');

INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (2, '민혁', 'cm@gmail.com', '341234', 'kakao', 'HOST');

INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (3, '카페 사장', 'owner@gmail.com', '23412', 'kakao', 'OWNER');

INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code,
                             address, image_url)
VALUES (1, 3, '카페 사장', '스타벅스', '123', '12', '12345', '서울', 'business-license-imgae.com');

INSERT INTO cafe (id, owner_id, business_license_id, name)
VALUES (1, 3, 1, '메가커피');

INSERT INTO artist (id, group_id, name, image_url)
VALUES (1, NULL, '아이유', 'image1.com');

-- 찜할 수 있는 생일 카페
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

-- 찜할 수 없는 생일 카페
INSERT INTO birthday_cafe (id, host_id, artist_id, cafe_id, cafe_owner_id,
                           start_date, end_date,
                           minimum_visitant, maximum_visitant,
                           twitter_account, host_phone_number,
                           progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (2, 2, 1, 1, 3,
        '2022-03-01 12:00:00', '2022-03-01 18:00:00',
        100, 200,
        'cafeTwitter', '010-1234-5678',
        'RENTAL_PENDING', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

-- 찜할 수 없는 생일 카페
INSERT INTO birthday_cafe (id, host_id, artist_id, cafe_id, cafe_owner_id,
                           start_date, end_date,
                           minimum_visitant, maximum_visitant,
                           twitter_account, host_phone_number,
                           progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (3, 2, 1, 1, 3,
        '2022-03-01 12:00:00', '2022-03-01 18:00:00',
        100, 200,
        'cafeTwitter', '010-1234-5678',
        'RENTAL_CANCELED', 'PRIVATE', 'SMOOTH', 'ABUNDANT');
