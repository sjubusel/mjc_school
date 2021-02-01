CREATE DATABASE gift_certificates_system;

CREATE TABLE gift_certificates_system.certificates
(
    id               BIGINT         NOT NULL PRIMARY KEY AUTO_INCREMENT UNIQUE,
    name             VARCHAR(256)   NOT NULL,
    description      VARCHAR(1024)  NOT NULL,
    price            DECIMAL(15, 2) NOT NULL,
    duration         INT(64)        NOT NULL,
    create_date      TIMESTAMP(3)   NOT NULL,
    last_update_date TIMESTAMP(3)   NOT NULL,
    is_deleted       boolean        NOT NULL,
    delete_date      TIMESTAMP(3)   NULL
);

CREATE TABLE gift_certificates_system.tags
(
    id          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT UNIQUE,
    name        VARCHAR(256) NOT NULL,
    is_deleted  boolean      NOT NULL,
    delete_date TIMESTAMP(3) NULL
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

INSERT INTO gift_certificates_system.certificates (name, description, price, duration, create_date, last_update_date)
VALUES ('Скалодром', 'Скалодром на Партизанском', 30, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- 1
       ('Одежда в подарок', 'H&M на Немиге', 300, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- 2
       ('Картинг', 'Картинг с самым большим треком в Республике Беларусь', 50, 5, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),                                                                     -- 3
       ('Пицца за 30%', 'Скидки 30% на весь перечень продукции в сети пиццерий Pizza ', 1.5, 7, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),                                                                     -- 4
       ('Мобильный телефон за старый', -- 5
        'Покупай новый флагман (мобильный телефон) за 100 рублей при сдаче более старого флагмана того же производителя',
        200, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO gift_certificates_system.tags (name)
VALUES ('Развлечения'),                -- 1
       ('Активный отдых'),             -- 2
       ('Скидки'),                     -- 3
       ('Продовольственные товары'),   -- 4
       ('Непродовольственные товары'), -- 5
       ('Техника'),                    -- 6
       ('Мода'); -- 7

INSERT INTO gift_certificates_system.join_certificates_tags_table (tag_id, certificate_id)
VALUES (1, 1), -- Развлечения → Скалодром
       (1, 3), -- Развлечения → Картинг
       (2, 1), -- Активный отдых → Скалодром
       (2, 3), -- Активный отдых → Картинг
       (3, 4), -- Скидки → Пицца за 30%
       (3, 5), -- Скидки → Мобильный телефон за старый
       (4, 4), -- Продовольственные товары →
       (5, 2), -- Непроводольственные товары → Одежда в подарок
       (5, 5), -- Непроводольственные товары → Мобильный телефон за старый
       (6, 5), -- Техника → Мобильный телефон за старый
       (7, 2);

CREATE TABLE gift_certificates_system.users
(
    id           BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT UNIQUE,
    first_name   VARCHAR(256) NOT NULL,
    last_name    VARCHAR(256) NOT NULL,
    email        VARCHAR(256) NOT NULL UNIQUE,
    phone_number VARCHAR(256) NOT NULL UNIQUE
);

INSERT INTO gift_certificates_system.users (first_name, last_name, email, phone_number)
VALUES ('Siarhei', 'Busel', 'email1@email.com', '+375 (29) 111-11-11'),
       ('Galileo', 'Galilei', 'email2@email.com', '+375 (29) 111-11-12'),
       ('William', 'Gilbert', 'email3@email.com', '+375 (29) 111-11-13'),
       ('Johannes', 'Kepler', 'email4@email.com', '+375 (29) 111-11-14'),
       ('Albert', 'Einstein', 'email5@email.com', '+375 (29) 111-11-15');

CREATE TABLE gift_certificates_system.orders
(
    id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT UNIQUE,
    order_date TIMESTAMP(3) NOT NULL,
    user_id    BIGINT       NOT NULL,

    CONSTRAINT fk__orders__users FOREIGN KEY (user_id) REFERENCES gift_certificates_system.users (id)
);

CREATE TABLE gift_certificates_system.order_positions
(
    id             BIGINT         NOT NULL PRIMARY KEY AUTO_INCREMENT UNIQUE,
    price          DECIMAL(15, 2) NOT NULL,
    order_id       BIGINT         NOT NULL,
    certificate_id BIGINT         NOT NULL,

    CONSTRAINT fk__order_positions__orders
        FOREIGN KEY (order_id) REFERENCES gift_certificates_system.orders (id),
    CONSTRAINT fk__order_positions__certificates
        FOREIGN KEY (certificate_id) REFERENCES gift_certificates_system.certificates (id)
);
