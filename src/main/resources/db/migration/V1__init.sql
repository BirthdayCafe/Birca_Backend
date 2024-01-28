CREATE TABLE member
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    nickname    VARCHAR(255)          NULL,
    email       VARCHAR(255)          NULL,
    member_role VARCHAR(255)          NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

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
    validation_code    INT                   NOT NULL,
    address            VARCHAR(255)          NOT NULL,
    detail_address     VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_businesslicense PRIMARY KEY (id)
);

ALTER TABLE business_license
    ADD CONSTRAINT FK_BUSINESSLICENSE_ON_OWNER FOREIGN KEY (owner_id) REFERENCES member (id);

CREATE TABLE cafe
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    business_license_id BIGINT                NOT NULL,
    CONSTRAINT pk_cafe PRIMARY KEY (id)
);

ALTER TABLE cafe
    ADD CONSTRAINT FK_CAFE_ON_BUSINESS_LICENSE FOREIGN KEY (business_license_id) REFERENCES business_license (id);
