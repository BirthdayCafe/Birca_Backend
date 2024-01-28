CREATE TABLE member
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    nickname    VARCHAR(255)          NULL,
    email       VARCHAR(255)          NULL,
    member_role VARCHAR(255)          NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);
