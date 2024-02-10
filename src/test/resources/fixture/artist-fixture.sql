INSERT INTO member (id, nickname, email, social_id, social_provider, member_role)
VALUES (1, '더즈', 'ldk@gmail.com', '231323', 'kakao', 'VISITANT');

INSERT INTO artist_group (id, name, image_url)
VALUES (5, '뉴진스', 'image5.com'),
       (6, '방탄소년단', 'image6.com'),
       (7, '비비지', 'image7.com'),
       (8, '세븐틴', 'image8.com'),
       (2, '스테이시', 'image2.com'),
       (11, '시스타', 'image11.com'),
       (12, '시스타', 'image12.com'),
       (3, '아이브', 'image3.com'),
       (1, '에스파', 'image1.com'),
       (9, '엑소', 'image9.com'),
       (4, '케플러', 'image4.com'),
       (10, '트와이스', 'image0.com');

INSERT INTO artist (id, group_id, name, image_url)
VALUES (1, 6, '석진', 'image1.com'),
       (2, 6, '정국', 'image2.com'),
       (3, 6, '제이홉', 'image3.com'),
       (4, 6, 'RM', 'image4.com'),
       (5, 6, '뷔', 'image5.com'),
       (6, 6, '지민', 'image6.com'),
       (7, 6, '슈가', 'image7.com'),

       (8, 5, '하니', 'image8.com'),
       (9, 5, '해린', 'image9.com'),
       (10, 5, '민지', 'image10.com'),
       (11, 5, '다니엘', 'image11.com'),
       (12, 5, '혜인', 'image12.com');
