CREATE TABLE member
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    nickname    VARCHAR(255)          NULL,
    email       VARCHAR(255)          NOT NULL,
    registration_id VARCHAR(255)      NOT NULL,
    member_role VARCHAR(255)          NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

ALTER TABLE member
    ADD CONSTRAINT uc_member_nickname UNIQUE (nickname);

ALTER TABLE member
    ADD CONSTRAINT uc_member_email UNIQUE (email);

CREATE TABLE business_license
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    owner_id           BIGINT                NOT NULL,
    owner_name         VARCHAR(255)          NOT NULL,
    cafe_name          VARCHAR(255)          NOT NULL,
    image_url          LONGTEXT              NOT NULL,
    tax_office_code    INT                   NOT NULL,
    business_type_code INT                   NOT NULL,
    serial_code        INT                   NOT NULL,
    address            VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_businesslicense PRIMARY KEY (id)
);

ALTER TABLE business_license
    ADD CONSTRAINT FK_BUSINESSLICENSE_ON_OWNER FOREIGN KEY (owner_id) REFERENCES member (id);

ALTER TABLE business_license
    ADD CONSTRAINT UNIQUE_BUSINESS_LICENSE_CODE UNIQUE (tax_office_code, business_type_code, serial_code);

CREATE TABLE cafe
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    business_license_id BIGINT                NOT NULL,
    CONSTRAINT pk_cafe PRIMARY KEY (id)
);

ALTER TABLE cafe
    ADD CONSTRAINT FK_CAFE_ON_BUSINESS_LICENSE FOREIGN KEY (business_license_id) REFERENCES business_license (id);

ALTER TABLE cafe
    ADD CONSTRAINT uc_cafe_businesslicenseid UNIQUE (business_license_id);

CREATE TABLE artist_group
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255)          NOT NULL,
    image_url LONGTEXT              NOT NULL,
    CONSTRAINT pk_artistgroup PRIMARY KEY (id)
);

CREATE INDEX IDX_NAME ON artist_group (name);

CREATE TABLE artist
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    group_id  BIGINT                NULL,
    name      VARCHAR(255)          NOT NULL,
    image_url LONGTEXT              NOT NULL,
    CONSTRAINT pk_artist PRIMARY KEY (id)
);

ALTER TABLE artist
    ADD CONSTRAINT FK_ARTIST_ON_GROUP FOREIGN KEY (group_id) REFERENCES artist_group (id);

CREATE TABLE favorite_artist
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    fan_id    BIGINT                NOT NULL,
    artist_id BIGINT                NOT NULL,
    CONSTRAINT pk_favoriteartist PRIMARY KEY (id)
);

ALTER TABLE favorite_artist
    ADD CONSTRAINT uc_favoriteartist_fan UNIQUE (fan_id);

ALTER TABLE favorite_artist
    ADD CONSTRAINT FK_FAVORITEARTIST_ON_ARTIST FOREIGN KEY (artist_id) REFERENCES artist (id);

ALTER TABLE favorite_artist
    ADD CONSTRAINT FK_FAVORITEARTIST_ON_FAN FOREIGN KEY (fan_id) REFERENCES member (id);

CREATE TABLE interest_artist
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    fan_id    BIGINT                NOT NULL,
    artist_id BIGINT                NOT NULL,
    CONSTRAINT pk_interestartist PRIMARY KEY (id)
);

ALTER TABLE interest_artist
    ADD CONSTRAINT FK_INTERESTARTIST_ON_ARTIST FOREIGN KEY (artist_id) REFERENCES artist (id);

ALTER TABLE interest_artist
    ADD CONSTRAINT FK_INTERESTARTIST_ON_FAN FOREIGN KEY (fan_id) REFERENCES member (id);

ALTER TABLE interest_artist
    ADD CONSTRAINT uc_interest_artist_fan_artist UNIQUE (fan_id, artist_id);

CREATE TABLE birthday_cafe
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    host_id          BIGINT                NOT NULL,
    artist_id        BIGINT                NOT NULL,
    cafe_id          BIGINT                NULL,
    twitter_account  VARCHAR(255)          NULL,
    start_date       datetime              NULL,
    end_date         datetime              NULL,
    minimum_visitant INT                   NULL,
    maximum_visitant INT                   NULL,
    CONSTRAINT pk_birthdaycafe PRIMARY KEY (id)
);

ALTER TABLE birthday_cafe
    ADD CONSTRAINT FK_BIRTHDAYCAFE_ON_ARTIST FOREIGN KEY (artist_id) REFERENCES artist (id);

ALTER TABLE birthday_cafe
    ADD CONSTRAINT FK_BIRTHDAYCAFE_ON_CAFE FOREIGN KEY (cafe_id) REFERENCES cafe (id);

ALTER TABLE birthday_cafe
    ADD CONSTRAINT FK_BIRTHDAYCAFE_ON_HOST FOREIGN KEY (host_id) REFERENCES member (id);
