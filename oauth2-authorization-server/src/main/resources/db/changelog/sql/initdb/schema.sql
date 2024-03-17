---------------------
-- Account schemas --
---------------------
CREATE SCHEMA account_data;

CREATE TABLE account_data.account(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_type VARCHAR(10) NOT NULL,
    name VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    password_expires_at TIMESTAMP,
    enabled BOOLEAN NOT NULL,
    CHECK (account_type IN ('customer', 'client'))
);

CREATE UNIQUE INDEX account_name_unique_index ON account_data.account(lower(name));
CREATE INDEX account_name_index ON account_data.account((lower(name)));

CREATE TABLE account_data.authority(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255)
);

CREATE UNIQUE INDEX unique_authority_name ON account_data.authority(lower(name));

CREATE TABLE account_data.account_authorities(
    account_id UUID REFERENCES account_data.account(id),
    authority_id INTEGER REFERENCES account_data.authority(id),
    PRIMARY KEY (account_id, authority_id)
);

--------------------
-- OAuth2 schemas --
--------------------

CREATE SCHEMA oauth2;

CREATE TABLE oauth2.access_token_type(
    id SERIAL PRIMARY KEY,
    value VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    CHECK (value IN ('OPAQUE', 'JWT'))
);

CREATE TABLE oauth2.client(
    id UUID PRIMARY KEY REFERENCES account_data.account(id),
    access_token_type_id INTEGER NOT NULL
        REFERENCES oauth2.access_token_type(id),
    name VARCHAR(80) NOT NULL,
    required_authorization_consent BOOLEAN NOT NULL,
    reuse_refresh_tokens BOOLEAN NOT NULL,
    required_proof_key BOOLEAN NOT NULL,
    issued_at TIMESTAMP NOT NULL,
    redirect_urls TEXT NOT NULL,
    access_token_time_live_millis BIGINT NOT NULL,
    authorization_code_time_live_millis BIGINT NOT NULL,
    refresh_token_time_live_millis BIGINT NOT NULL,
    device_code_time_live_millis BIGINT NOT NULL,
    post_logout_urls TEXT,
    jwk_set_url TEXT,
    token_algorithm VARCHAR(15),
    logo_url TEXT
);

CREATE TABLE oauth2.scope(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    description VARCHAR(255)
);

CREATE UNIQUE INDEX scope_name_unique_index ON oauth2.scope((lower(name)));

CREATE TABLE oauth2.client_scopes(
    client_id UUID CONSTRAINT client_scopes_client_id_fk
        REFERENCES oauth2.client(id),
    scope_id BIGINT CONSTRAINT client_scopes_scope_id_fk
        REFERENCES oauth2.scope(id),
    PRIMARY KEY (client_id, scope_id)
);

CREATE TABLE oauth2.grant_type(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    CHECK (name IN ('CLIENT_CREDENTIALS', 'REFRESH_TOKEN',
        'AUTHORIZATION_CODE', 'JWT_BEARER', 'DEVICE_CODE'))
);

CREATE TABLE oauth2.client_grant_types(
    client_id UUID CONSTRAINT client_grant_types_client_id_fk
        REFERENCES oauth2.client(id),
    grant_type_id INTEGER CONSTRAINT client_grant_types_grant_type_id_fk
        REFERENCES oauth2.grant_type(id),
    PRIMARY KEY (client_id, grant_type_id)
);

CREATE TABLE oauth2.authentication_method(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    CHECK (name IN ('CLIENT_SECRET_BASIC',
        'CLIENT_SECRET_POST', 'CLIENT_SECRET_JWT',
        'PRIVATE_KEY_JWT', 'NONE'))
);

CREATE TABLE oauth2.client_authentication_methods(
    client_id UUID CONSTRAINT client_authentication_methods_client_id_fk
        REFERENCES oauth2.client(id),
    authentication_method_id INTEGER CONSTRAINT client_authentication_methods_authentication_method_id_fk
        REFERENCES oauth2.grant_type(id),
    PRIMARY KEY (client_id, authentication_method_id)
);

CREATE TABLE oauth2.authorization(
  id UUID PRIMARY KEY,
  client_id UUID NOT NULL REFERENCES oauth2.client(id),
  account_id UUID NOT NULL REFERENCES account_data.account(id),
  grant_type VARCHAR(100) NOT NULL,
  scopes VARCHAR(1000),
  attributes TEXT,
  state VARCHAR(1000),
  authorization_code_value TEXT,
  authorization_code_issued_at TIMESTAMP,
  authorization_code_expires_at TIMESTAMP,
  authorization_code_metadata TEXT,
  access_token_value TEXT,
  access_token_issued_at TIMESTAMP,
  access_token_expires_at TIMESTAMP,
  access_token_metadata TEXT,
  access_token_scopes VARCHAR(1000),
  access_token_type VARCHAR(100),
  refresh_token_value TEXT,
  refresh_token_issued_at TIMESTAMP,
  refresh_token_expires_at TIMESTAMP,
  refresh_token_metadata TEXT,
  oidc_id_token_value TEXT,
  oidc_id_token_issued_at TIMESTAMP,
  oidc_id_token_expires_at TIMESTAMP,
  oidc_id_token_metadata TEXT,
  user_code_value TEXT,
  user_code_issued_at TIMESTAMP,
  user_code_expires_at TIMESTAMP,
  user_code_metadata TEXT,
  device_code_value TEXT,
  device_code_issued_at TIMESTAMP,
  device_code_expires_at TIMESTAMP,
  device_code_metadata TEXT
);

CREATE TABLE oauth2.authorization_consent(
  client_id UUID REFERENCES oauth2.client(id),
  account_id UUID REFERENCES account_data.account(id),
  PRIMARY KEY (client_id, account_id)
);

CREATE TABLE oauth2.authorization_consent_scopes(
    client_id UUID,
    account_id UUID,
    scope_id BIGINT REFERENCES oauth2.scope(id),
    FOREIGN KEY (client_id, account_id) REFERENCES oauth2.authorization_consent(client_id, account_id),
    PRIMARY KEY (client_id, account_id, scope_id)
);
