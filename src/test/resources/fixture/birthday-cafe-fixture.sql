SET foreign_key_checks = 0;

-- 주최자1
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'HOST'), -- 주최자1
       (2, '민혁', 'cm@gmail.com', '341234', 'kakao', 'HOST'), -- 주최자2
       (3, '카페1 사장', 'owner@gmail.com', '23412', 'kakao', 'OWNER'), -- 사장님

       (4, '방문자', 'visitant@gmail.com', '232', 'kakao', 'VISITANT'), -- 방문자
       (5, '카페2 사장', 'owner2@gmail.com', '23422', 'kakao', 'DELETED');

-- 사장님의 사업자 등록증
INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code,
                             address, image_url, registration_approved)
VALUES (1, 3, '카페 사장', '스타벅스', '123', '12', '12345', '서울', 'business-license-imgae.com', false);

-- 사장님의 카페
INSERT INTO cafe (id, owner_id, business_license_id, name, address, twitter_account, business_hours)
VALUES (1, 3, 1, '메가커피', '서울특별시 강남구 테헤란로 212', '@ChaseM', '9시 - 22시'), -- 사장님의 카페
       (2, 4, 2, '스타벅스', '경기도 성남시 분당구 판교역로 235', '@ChaseM', '9시 - 22시'); -- 다른 카페

INSERT INTO cafe_image(id, cafe_id, image_url)
VALUES (1, 1, 'image1.com'),
       (2, 1, 'image2.com');

-- 아티스트
INSERT INTO artist_group (id, name, image_url)
VALUES (1, '에스파', 'aespa-image.com');

INSERT INTO artist (id, group_id, name, image_url)
VALUES (1, null, '아이유', 'image1.com'),
       (2, null, '윤종신', 'image1.com'),
       (3, 1, '윈터', 'image1.com');

-- 생일 카페
INSERT INTO birthday_cafe
(id, name, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant,
 twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (1, null, 1, 1, 1, 3, '2024-02-08T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_PENDING','PRIVATE', 'SMOOTH', 'ABUNDANT'), -- 주최자1의 대관 대기 카페, 비공개

       (2, '윤종신의 생일 카페', 2, 2, 1, 3, '2024-02-08T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'FINISHED','PUBLIC', 'SMOOTH', 'ABUNDANT'), -- 주최자1의 종료된 카페
       (3, '아이유의 생일 카페', 1, 1, 1, 3, '2024-02-09T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'FINISHED','PUBLIC', 'SMOOTH', 'ABUNDANT'), -- 주최자1의 종료된 카페
       (4, '윈터의 생일 카페', 1, 3, 2, 3, '2024-02-10T00:00:00', '2024-02-11T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'IN_PROGRESS','PUBLIC', 'SMOOTH', 'ABUNDANT'), -- 주최자1의 진행 중 카페
       (5, '윈터의 생일 카페', 2, 3, 1, 3, '2024-02-11T00:00:00', '2024-02-12T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'RENTAL_APPROVED','PUBLIC', 'SMOOTH', 'ABUNDANT'), -- 주최자1의 대관 승인 카페
       (6, null, 2, 1, 2, 3, '2024-02-11T00:00:00', '2024-02-14T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'IN_PROGRESS','PUBLIC', 'SMOOTH', 'ABUNDANT'),
       (7, '윈터의 생일 카페', 2, 1, 2, 5, '2024-02-11T00:00:00', '2024-02-14T00:00:00', 5, 10, '@ChaseM', '010-0000-0000',
        'IN_PROGRESS','PUBLIC', 'SMOOTH', 'ABUNDANT');


-- 주최자1의 진행 중 생일 카페 특전, 메뉴, 럭키 드로우
INSERT INTO special_goods
    (birthday_cafe_id, name, details)
VALUES (4, '특전', '포토카드'),
       (4, '디저트', '포토카드, ID 카드');

INSERT INTO birthday_cafe_menu
    (birthday_cafe_id, name, details, price)
VALUES (4, '기본', '아메리카노+포토카드+ID카드', 6000),
       (4, '디저트', '케이크+포토카드+ID카드', 10000);

INSERT INTO lucky_draw
    (birthday_cafe_id, ranks, prize)
VALUES (4, 1, '티셔츠'),
       (4, 2, '머그컵');

-- 주최자1의 진행 중 생일 카페 이미지
INSERT INTO birthday_cafe_image (id, birthday_cafe_id, image_url, is_main)
VALUES (1, 4, 'winter-cafe-default-image', false),
       (2, 4, 'winter-cafe-main-image.com', true),
       (3, 4, 'winter-cafe-default-image', false),
       (4, 3, 'iu-cafe-main-image.com', true),
       (5, 3, 'iu-cafe-default-image.com', false),
       (6, 3, 'iu-cafe-default-image.com', false);

-- 방문자(id=4)가 누른 찜
INSERT INTO likes (visitant_id, target_id, target_type)
VALUES (4, 3, 'BIRTHDAY_CAFE'),
       (4, 6, 'BIRTHDAY_CAFE');

INSERT INTO day_off (id, cafe_id, day_off_date)
VALUES (1, 1, '2024-03-08T00:00:00');

INSERT INTO memo (id, birthday_cafe_id, content)
VALUES (1, 6, '생일 카페 메모 내용');

SET foreign_key_checks = 1;
