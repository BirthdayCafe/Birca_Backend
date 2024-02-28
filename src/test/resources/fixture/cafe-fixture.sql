SET foreign_key_checks = 0;

INSERT INTO cafe (id, owner_id, business_license_id, name)
VALUES (1, 1, 1, '미스티우드'),
       (2, 2, 2, '우지커피'),
       (3, 3, 3, '메가커피');

SET foreign_key_checks = 1;
