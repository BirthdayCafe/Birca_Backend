SET foreign_key_checks = 0;

INSERT INTO cafe (id, owner_id, business_license_id, name, address, twitter_account, business_hours)
VALUES (1, 1, 1, '미스티우드', '경기도 시흥시 은계중앙로 115', '@ChaseM', '6시 - 22시'),
       (2, 2, 2, '우지커피', '경기도 성남시 분당구 판교역로 235', '@ChaseM', '8시 - 22시'),
       (3, 3, 3, '메가커피', '서울특별시 강남구 테헤란로 212', '@ChaseM', '9시 - 22시');

SET foreign_key_checks = 1;
