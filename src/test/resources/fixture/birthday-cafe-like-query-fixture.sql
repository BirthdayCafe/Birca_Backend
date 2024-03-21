-- 찜하기를 누를 회원
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'VISITANT');

INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (2, '카페 사장', 'owner@gmail.com', '23412', 'kakao', 'OWNER');

INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code,
                             address, image_url)
VALUES (1, 2, '카페 사장', '스타벅스', '123', '12', '12345', '서울', 'business-license-imgae.com');

INSERT INTO cafe (id, owner_id, business_license_id, name)
VALUES (1, 2, 1, '메가커피');

-- 아티스트
INSERT INTO artist_group (id, name, image_url)
VALUES (1, '샤이니', 'group-image.com');

INSERT INTO artist (id, group_id, name, image_url)
VALUES (1, 1, '민호', 'image1.com'),
       (2, null, '아이유', 'image2.com');

-- 찜할 생일 카페
INSERT INTO birthday_cafe
(id, name, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant,
 twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (1, '민호 생일 카페', 1, 1, 1, 2, '2024-03-18T00:00:00', '2024-03-19T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'IN_PROGRESS', 'PRIVATE', 'SMOOTH', 'ABUNDANT'),
       (2, '아이유 생일 카페', 1, 2, 1, 2, '2024-03-20T00:00:00', '2024-03-23T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_PENDING', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

INSERT INTO birthday_cafe_image
(id, birthday_cafe_id, image_url, is_main)
VALUES (1, 1, 'image1.com', true),
       (2, 2, 'image2.com', true);

INSERT INTO birthday_cafe_like
(id, birthday_cafe_id, visitant_id)
VALUES (1, 1, 1),
       (2, 2, 1);
