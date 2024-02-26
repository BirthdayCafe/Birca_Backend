SET foreign_key_checks = 0;

INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'HOST');

INSERT INTO cafe (id, business_license_id)
VALUES (1, 1);

INSERT INTO cafe (id, business_license_id)
VALUES (2, 2);

INSERT INTO artist (id, group_id, name, image_url)
VALUES (1, null, '아이유', 'image1.com');

SET foreign_key_checks = 1;
