CREATE SCHEMA gift_certificates_system;

CREATE TABLE gift_certificates_system.certificates
(
    id   BIGINT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(256)   NOT NULL,
    description      VARCHAR(1024)  NOT NULL,
    price            DECIMAL(15, 2) NOT NULL,
    duration         INT(64)        NOT NULL,
    create_date      TIMESTAMP      NOT NULL,
    last_update_date TIMESTAMP      NOT NULL
);

CREATE TABLE gift_certificates_system.tags
(
    id BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name   VARCHAR(256) NOT NULL
);

CREATE TABLE gift_certificates_system.join_certificates_tags_table
(
    certificate_id BIGINT NOT NULL,
    tag_id         BIGINT NOT NULL
);

ALTER TABLE gift_certificates_system.join_certificates_tags_table
    ADD CONSTRAINT pk__tag_id__certificate_id PRIMARY KEY (tag_id, certificate_id);

ALTER TABLE gift_certificates_system.join_certificates_tags_table
    ADD CONSTRAINT fk__join_certificates_tags_table__tags
        FOREIGN KEY (tag_id) REFERENCES gift_certificates_system.tags (id);

ALTER TABLE gift_certificates_system.join_certificates_tags_table
    ADD CONSTRAINT fk__join_certificates_tags_table__certificates
        FOREIGN KEY (certificate_id) REFERENCES gift_certificates_system.certificates (id);