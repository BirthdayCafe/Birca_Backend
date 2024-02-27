SET foreign_key_checks = 0;

INSERT INTO cafe (id, business_license_id, name)
VALUES (1, 1, '미스티 우드'),
       (2, 2, '우지 커피'),
       (3, 3, '메가 커피');

SET foreign_key_checks = 1;
