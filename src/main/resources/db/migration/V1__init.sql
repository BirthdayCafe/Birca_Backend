CREATE TABLE member
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    email           VARCHAR(255) NOT NULL,
    member_role     VARCHAR(255) NOT NULL,
    nickname        VARCHAR(255) NULL,
    social_id       VARCHAR(255) NOT NULL,
    social_provider VARCHAR(255) NOT NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

ALTER TABLE member
    ADD CONSTRAINT UC_IDENTITY_KEY UNIQUE (social_id, social_provider);

ALTER TABLE member
    ADD CONSTRAINT uc_member_nickname UNIQUE (nickname);

CREATE TABLE business_license
(
    id                    BIGINT AUTO_INCREMENT NOT NULL,
    owner_id              BIGINT       NOT NULL,
    owner_name            VARCHAR(255) NOT NULL,
    cafe_name             VARCHAR(255) NOT NULL,
    image_url             LONGTEXT     NOT NULL,
    tax_office_code       VARCHAR(255) NOT NULL,
    business_type_code    VARCHAR(255) NOT NULL,
    serial_code           VARCHAR(255) NOT NULL,
    address               VARCHAR(255) NOT NULL,
    registration_approved TINYINT(1) NOT NULL,
    CONSTRAINT pk_businesslicense PRIMARY KEY (id)
);

ALTER TABLE business_license
    ADD CONSTRAINT FK_BUSINESSLICENSE_ON_OWNER FOREIGN KEY (owner_id) REFERENCES member (id);

CREATE TABLE cafe
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    business_license_id BIGINT       NOT NULL,
    owner_id            BIGINT       NOT NULL,
    name                VARCHAR(255) NOT NULL,
    address             VARCHAR(255) NULL,
    twitter_account     VARCHAR(255) NULL,
    business_hours      VARCHAR(255) NULL,
    CONSTRAINT pk_cafe PRIMARY KEY (id)
);

ALTER TABLE cafe
    ADD CONSTRAINT FK_CAFE_ON_BUSINESS_LICENSE FOREIGN KEY (business_license_id) REFERENCES business_license (id);

ALTER TABLE cafe
    ADD CONSTRAINT FK_CAFE_ON_MEMBER FOREIGN KEY (owner_id) REFERENCES member (id);

ALTER TABLE cafe
    ADD CONSTRAINT uc_cafe_businesslicenseid UNIQUE (business_license_id);

CREATE TABLE day_off
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    cafe_id      BIGINT   NOT NULL,
    day_off_date DATETIME NOT NULL,
    CONSTRAINT pk_day_off PRIMARY KEY (id)
);

ALTER TABLE day_off
    ADD CONSTRAINT FK_DAY_OFF_ON_CAFE FOREIGN KEY (cafe_id) REFERENCES cafe (id);

CREATE TABLE cafe_image
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    cafe_id   BIGINT   NOT NULL,
    image_url LONGTEXT NOT NULL,
    CONSTRAINT pk_cafe_image PRIMARY KEY (id)
);

ALTER TABLE cafe_image
    ADD CONSTRAINT FK_CAFE_IMAGE_ON_CAFE FOREIGN KEY (cafe_id) REFERENCES cafe (id);

CREATE TABLE cafe_menu
(
    cafe_id BIGINT       NOT NULL,
    name    VARCHAR(255) NOT NULL,
    price   INT          NOT NULL
);

ALTER TABLE cafe_menu
    ADD CONSTRAINT FK_CAFE_MENU_ON_CAFE FOREIGN KEY (cafe_id) REFERENCES cafe (id);

CREATE TABLE cafe_option
(
    cafe_id BIGINT       NOT NULL,
    name    VARCHAR(255) NOT NULL,
    price   INT          NOT NULL
);

ALTER TABLE cafe_option
    ADD CONSTRAINT FK_CAFE_OPTION_ON_CAFE FOREIGN KEY (cafe_id) REFERENCES cafe (id);

CREATE TABLE artist_group
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255) NOT NULL,
    image_url LONGTEXT     NOT NULL,
    CONSTRAINT pk_artistgroup PRIMARY KEY (id)
);

CREATE INDEX IDX_NAME ON artist_group (name);

CREATE TABLE artist
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    group_id  BIGINT NULL,
    name      VARCHAR(255) NOT NULL,
    image_url LONGTEXT     NOT NULL,
    CONSTRAINT pk_artist PRIMARY KEY (id)
);

ALTER TABLE artist
    ADD CONSTRAINT FK_ARTIST_ON_GROUP FOREIGN KEY (group_id) REFERENCES artist_group (id);

CREATE TABLE favorite_artist
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    fan_id    BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,
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
    fan_id    BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,
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
    id                        BIGINT AUTO_INCREMENT NOT NULL,
    host_id                   BIGINT NULL,
    artist_id                 BIGINT       NOT NULL,
    cafe_id                   BIGINT       NOT NULL,
    cafe_owner_id             BIGINT       NOT NULL,
    twitter_account           VARCHAR(255) NULL,
    name                      VARCHAR(255) NULL,
    start_date                datetime NULL,
    end_date                  datetime NULL,
    minimum_visitant          INT          NOT NULL,
    maximum_visitant          INT          NOT NULL,
    host_phone_number         VARCHAR(255) NOT NULL,
    progress_state            VARCHAR(255) NOT NULL,
    visibility                VARCHAR(255) NOT NULL,
    congestion_state          VARCHAR(255) NOT NULL,
    special_goods_stock_state VARCHAR(255) NOT NULL,
    CONSTRAINT pk_birthdaycafe PRIMARY KEY (id)
);

ALTER TABLE birthday_cafe
    ADD CONSTRAINT FK_BIRTHDAYCAFE_ON_ARTIST FOREIGN KEY (artist_id) REFERENCES artist (id);

ALTER TABLE birthday_cafe
    ADD CONSTRAINT FK_BIRTHDAYCAFE_ON_CAFE FOREIGN KEY (cafe_id) REFERENCES cafe (id);

ALTER TABLE birthday_cafe
    ADD CONSTRAINT FK_BIRTHDAYCAFE_ON_HOST FOREIGN KEY (host_id) REFERENCES member (id);

CREATE INDEX idx_start_end_date ON birthday_cafe (start_date, end_date);

CREATE TABLE special_goods
(
    birthday_cafe_id BIGINT       NOT NULL,
    name             VARCHAR(255) NOT NULL,
    details          VARCHAR(255) NOT NULL
);

ALTER TABLE special_goods
    ADD CONSTRAINT fk_special_goods_on_birthday_cafe FOREIGN KEY (birthday_cafe_id) REFERENCES birthday_cafe (id);

CREATE TABLE birthday_cafe_menu
(
    birthday_cafe_id BIGINT       NOT NULL,
    name             VARCHAR(255) NOT NULL,
    details          VARCHAR(255) NOT NULL,
    price            INT          NOT NULL
);

ALTER TABLE birthday_cafe_menu
    ADD CONSTRAINT fk_menu_on_birthday_cafe FOREIGN KEY (birthday_cafe_id) REFERENCES birthday_cafe (id);

CREATE TABLE lucky_draw
(
    birthday_cafe_id BIGINT       NOT NULL,
    ranks            INT          NOT NULL,
    prize            VARCHAR(255) NOT NULL
);

ALTER TABLE lucky_draw
    ADD CONSTRAINT fk_lucky_draw_on_birthday_cafe FOREIGN KEY (birthday_cafe_id) REFERENCES birthday_cafe (id);

CREATE TABLE ocr_request_history
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    owner_id     BIGINT NOT NULL,
    upload_count INT    NOT NULL,
    CONSTRAINT pk_ocrrequesthistory PRIMARY KEY (id)
);

ALTER TABLE ocr_request_history
    ADD CONSTRAINT FK_OCRREQUESTHISTORY_ON_OWNER FOREIGN KEY (owner_id) REFERENCES member (id);

CREATE TABLE birthday_cafe_image
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    birthday_cafe_id BIGINT   NOT NULL,
    image_url        LONGTEXT NOT NULL,
    is_main          TINYINT(1) NOT NULL,
    CONSTRAINT pk_birthdaycafeimage PRIMARY KEY (id)
);

ALTER TABLE birthday_cafe_image
    ADD CONSTRAINT fk_birthday_cafe_image_on_birthday_cafe FOREIGN KEY (birthday_cafe_id) REFERENCES birthday_cafe (id);

CREATE TABLE likes
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    visitant_id BIGINT       NOT NULL,
    target_id   BIGINT       NOT NULL,
    target_type VARCHAR(255) NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (id)
);

ALTER TABLE likes
    ADD CONSTRAINT uc_like_target UNIQUE (visitant_id, target_id, target_type);

CREATE TABLE memo
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    birthday_cafe_id BIGINT NOT NULL,
    content          LONGTEXT NULL,
    CONSTRAINT pk_memo PRIMARY KEY (id)
);

ALTER TABLE memo
    ADD CONSTRAINT fk_memo_on_birthday_cafe FOREIGN KEY (birthday_cafe_id) REFERENCES birthday_cafe (id);
