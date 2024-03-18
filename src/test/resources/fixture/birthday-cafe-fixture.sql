SET foreign_key_checks = 0;

-- 주최자1
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'HOST');

-- 주최자2
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (2, '민혁', 'cm@gmail.com', '341234', 'kakao', 'HOST');

-- 사장님
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (3, '카페1 사장', 'owner@gmail.com', '23412', 'kakao', 'OWNER');

-- 사장님의 사업자 등록증
INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code, address, image_url)
VALUES (1, 3, '카페 사장', '스타벅스', '123', '12', '12345', '서울', 'business-license-imgae.com');

-- 사장님의 카페
INSERT INTO cafe (id, owner_id, business_license_id, name)
VALUES (1, 3, 1, '메가커피');

-- 다른 카페
INSERT INTO cafe (id, owner_id, business_license_id, name)
VALUES (2, 4, 2, '스타벅스');

-- 아티스트
INSERT INTO artist (id, group_id, name, image_url)
VALUES (1, null, '아이유', 'image1.com');

-- 주최자1의 대관 대기 중 생일 카페
INSERT INTO birthday_cafe
    (id, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant, twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (1, 1, 1, 1, 3, '2024-02-08T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000', 'RENTAL_PENDING', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

-- 주최자1의 진행 중인 생일 카페
INSERT INTO birthday_cafe
(id, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant, twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (2, 1, 1, 1, 3, '2024-02-08T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000', 'IN_PROGRESS', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

INSERT INTO special_goods
(birthday_cafe_id, name, details)
VALUES (2, '특전', '포토카드'), (2, '디저트', '포토카드, ID 카드');

INSERT INTO menu
(birthday_cafe_id, name, details, price)
VALUES (2, '기본', '아메리카노+포토카드+ID카드', 6000), (2, '디저트', '케이크+포토카드+ID카드', 10000);

INSERT INTO lucky_draw
(birthday_cafe_id, ranks, prize)
VALUES (2, 1, '티셔츠'), (2, 2, '머그컵');

--- 주최자2의 진행 중인 생일 카페
INSERT INTO birthday_cafe
(id, host_id, artist_id, cafe_id, cafe_owner_id, start_date, end_date, minimum_visitant, maximum_visitant, twitter_account, host_phone_number, progress_state, visibility, congestion_state, special_goods_stock_state)
VALUES (3, 2, 1, 1, 3, '2024-02-08T00:00:00', '2024-02-10T00:00:00', 5, 10, '@ChaseM', '010-0000-0000', 'IN_PROGRESS', 'PRIVATE', 'SMOOTH', 'ABUNDANT');

SET foreign_key_checks = 1;
