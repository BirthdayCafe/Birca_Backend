INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'VISITANT');

INSERT INTO artist_group (id, name, image_url)
VALUES (5, '뉴진스', 'image5.com');

INSERT INTO artist (id, group_id, name, image_url)
VALUES (8, 5, '하니', 'image8.com'),
       (9, 5, '해린', 'image9.com'),
       (10, 5, '민지', 'image10.com'),
       (11, 5, '다니엘', 'image11.com'),
       (12, 5, '혜인', 'image12.com');

INSERT INTO favorite_artist (id, fan_id, artist_id)
VALUES (1, 1, 10);
