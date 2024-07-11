SET foreign_key_checks = 0;

-- 주최자1
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'HOST'),       -- 주최자1
       (2, '카페1 사장', 'owner@gmail.com', '23412', 'kakao', 'OWNER'); -- 사장님

-- 사장님의 카페
INSERT INTO cafe (id, owner_id, business_license_id, name, address, twitter_account, business_hours)
VALUES (1, 3, 1, '메가커피', '서울특별시 강남구 테헤란로 212', '@ChaseM', '9시 - 22시'); -- 사장님의 카페

-- 아티스트
INSERT INTO artist_group (id, name, image_url)
VALUES (1, '에스파', 'aespa-image.com');

INSERT INTO artist (id, group_id, name, image_url)
VALUES (1, null, '아이유', 'image1.com'),
       (2, null, '윤종신', 'image1.com'),
       (3, 1, '윈터', 'image1.com'),
       (4, 1, '카리나', 'image1.com');

-- 생일 카페
INSERT INTO birthday_cafe
(id, name, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant,
 twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (1, null, 1, 1, 1, 2, '2024-02-08T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_PENDING', 'PRIVATE', 'SMOOTH', 'ABUNDANT'), -- 주최자1의 대관 대기 카페, 비공개
       (2, '윤종신의 생일 카페', 1, 2, 1, 2, '2024-02-08T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'IN_PROGRESS', 'PUBLIC', 'SMOOTH', 'ABUNDANT'),        -- 주최자1의 종료된 카페
       (3, '아이유의 생일 카페', 1, 1, 1, 2, '2024-02-07T00:00:00', '2024-02-09T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_APPROVED', 'PUBLIC', 'SMOOTH', 'ABUNDANT'),        -- 주최자1의 종료된 카페
       (4, '카리나의 생일 카페', 1, 1, 2, 2, '2024-03-11T00:00:00', '2024-03-14T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_PENDING', 'PUBLIC', 'SMOOTH', 'ABUNDANT'),
       (5, '카리나의 생일 카페', null, 4, 2, 2, '2024-03-15T00:00:00', '2024-03-16T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_APPROVED', 'PUBLIC', 'SMOOTH', 'ABUNDANT');

SET foreign_key_checks = 1;
