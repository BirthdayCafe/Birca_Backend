SET foreign_key_checks = 0;

INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'OWNER'),
       (101, '민혁', 'gur@gmail.com', '41221', 'apple', 'OWNER'),
       (102, '사장', 'owner@gmail.com', '45221', 'apple', 'OWNER');

INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code,
                             address, image_url, registration_approved)
VALUES (1, 101, '카페 사장', '스타벅스', '123', '12', '12345', '서울', 'business-license-imgae.com', true),
       (2, 102, '카페 사장', '메가커피', '321', '21', '54321', '서울', 'business-license-imgae.com', false);

SET foreign_key_checks = 1;