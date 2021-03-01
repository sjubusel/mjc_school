CREATE TABLE oauth_client_details
(
    client_id               VARCHAR(256) PRIMARY KEY,
    resource_ids            VARCHAR(256),
    client_secret           VARCHAR(256),
    scope                   VARCHAR(256),
    authorized_grant_types  VARCHAR(256),
    web_server_redirect_uri VARCHAR(256),
    authorities             VARCHAR(256),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    autoapprove             VARCHAR(256)
);
``
INSERT INTO oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
VALUES ('gift-certificates-system-id-code', '$2y$11$IRPR00oTOc4R6GSejOhI7utCOON.BGHX1OcRAL2/OndYxnzN42K02',
        'read,write,gift_certificates_system', 'authorization_code',
        'http://localhost:9999/login/oauth2/callback/gcs_code', 'USER,ADMIN',
        36000, 36000, null, false),
       ('gift-certificates-system-id-rops', '$2y$11$IRPR00oTOc4R6GSejOhI7utCOON.BGHX1OcRAL2/OndYxnzN42K02',
        'read,write,gift_certificates_system', 'password',
        'http://localhost:9999/login/oauth2/callback/gcs_ropc', 'USER,ADMIN',
        36000, 36000, null, false);
