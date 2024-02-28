SET foreign_key_checks = 0;

-- 주최자
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'HOST');

-- 그냥 회원
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (2, '민혁', 'cm@gmail.com', '341234', 'kakao', 'HOST');

-- 사장님
INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (3, '카페1 사장', 'owner@gmail.com', '23412', 'kakao', 'OWNER');

-- 사장님의 사업자 등록증
INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code, address, image_url)
VALUES (1, 3, '카페 사장', '스타벅스', '123', '12', '12345', '서울', 'business-license-imgae.com');

-- 사장님의 카페
INSERT INTO cafe (id, business_license_id, owner_id)
VALUES (1, 1, 3);

-- 다른 카페
INSERT INTO cafe (id, business_license_id, owner_id)
VALUES (2, 2, 4);

-- 아티스트
INSERT INTO artist (id, group_id, name, image_url)
VALUES (1, null, '아이유', 'image1.com');

SET foreign_key_checks = 1;
