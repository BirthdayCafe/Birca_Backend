INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'OWNER');

INSERT INTO business_license(id, owner_id, owner_name, cafe_name, tax_office_code, business_type_code, serial_code,
                             address, image_url, registration_approved)
VALUES (1, 1, '더즈', '스타벅스', '123', '12', '12345', '서울특별시 강남구 테헤란로 212', 'business-license-imgae.com', false);