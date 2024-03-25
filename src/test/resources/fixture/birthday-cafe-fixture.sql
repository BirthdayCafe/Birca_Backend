SET foreign_key_checks = 0;

-- 주최자1
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'HOST'), -- 주최자1
       (2, '민혁', 'cm@gmail.com', '341234', 'kakao', 'HOST'), -- 주최자2
       (3, '카페1 사장', 'owner@gmail.com', '23412', 'kakao', 'OWNER'), -- 사장님

       (4, '방문자', 'visitant@gmail.com', '232', 'kakao', 'VISITANT'); -- 방문자

-- 사장님의 사업자 등록증
INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code,
                             address, image_url)
VALUES (1, 3, '카페 사장', '스타벅스', '123', '12', '12345', '서울', 'business-license-imgae.com');

-- 사장님의 카페
INSERT INTO cafe (id, owner_id, business_license_id, name)
VALUES (1, 3, 1, '메가커피'), -- 사장님의 카페
       (2, 4, 2, '스타벅스'); -- 다른 카페

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
        'IN_PROGRESS','PUBLIC', 'SMOOTH', 'ABUNDANT'); -- 주최자 2의 진행 중인 카페


-- 주최자1의 진행 중 생일 카페 특전, 메뉴, 럭키 드로우
INSERT INTO special_goods
    (birthday_cafe_id, name, details)
VALUES (4, '특전', '포토카드'),
       (4, '디저트', '포토카드, ID 카드');

INSERT INTO menu
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
       (3, 4, 'winter-cafe-default-image', false);

-- 방문자(id=4)가 누른 찜
INSERT INTO likes (visitant_id, target_id, target_type)
VALUES (4, 3, 'BIRTHDAY_CAFE');

SET
foreign_key_checks = 1;
