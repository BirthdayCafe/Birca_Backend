SET foreign_key_checks = 0;

INSERT INTO cafe (id, owner_id, business_license_id, name, address, twitter_account, business_hours)
VALUES (1, 1, 1, '미스티우드', '경기도 시흥시 은계중앙로 115', '@ChaseM', '6시 - 22시'),
       (2, 1, 2, '메가커피', '경기도 시흥시 은계중앙로 430', '@ChaseM', '8시 - 22시');

INSERT INTO cafe_image (id, cafe_id, image_url)
VALUES (1, 1, 'image1.com'),
       (2, 1, 'image2.com'),
       (3, 1, 'image3.com'),
       (4, 1, 'image4.com'),
       (5, 1, 'image5.com');

SET foreign_key_checks = 1;
