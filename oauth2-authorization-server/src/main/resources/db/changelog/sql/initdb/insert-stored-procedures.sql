CREATE OR REPLACE PROCEDURE account_data.create_account(
    name VARCHAR(80), bcrypt_password VARCHAR(255),
    enabled BOOLEAN, authority_names VARCHAR[])
LANGUAGE plpgsql AS $$
DECLARE
    authority_name VARCHAR(50);
    authority_id INTEGER;
    account_id UUID;
BEGIN
    INSERT INTO account_data.account(name, password, enabled, account_type)
    VALUES(name, bcrypt_password, enabled, 'customer') RETURNING id INTO account_id;
    IF (authority_names IS NULL) OR (cardinality(authority_names) = 0) THEN
        RETURN;
    END IF;
    FOREACH authority_name IN ARRAY authority_names LOOP
        SELECT a.id FROM account_data.authority a
        WHERE a.name = authority_name INTO authority_id;
        IF authority_id IS NULL THEN
            RAISE EXCEPTION 'Non-existent name --> %', authority_name
                USING HINT = 'Please check authority name';
        ELSE
            INSERT INTO account_data.account_authorities(account_id, authority_id)
            VALUES(account_id, authority_id);
        END IF;
    END LOOP;
END; $$;

CREATE OR REPLACE PROCEDURE oauth2.create_client(
    access_token_type VARCHAR(20), name VARCHAR(80),
    client_id VARCHAR(80), bcrypt_client_secret VARCHAR(255),
    required_authorization_consent BOOLEAN, reuse_refresh_tokens BOOLEAN,
    required_proof_key BOOLEAN, issued_at TIMESTAMP, secret_expires_at TIMESTAMP,
    redirect_urls TEXT, post_logout_urls TEXT, scopes VARCHAR[], grant_types VARCHAR[],
    authentication_methods VARCHAR[])
LANGUAGE plpgsql AS $$
DECLARE
    -- 5 minutes
    authorization_code_time_live_millis BIGINT := 60 * 5 * 1000;
    -- 15 minutes
    access_token_time_live_millis BIGINT := 60 * 15 * 1000;
    -- 15 days
    refresh_token_time_live_millis BIGINT := 60 * 1000 * 60 * 24 * 15;
    access_token_type_id INTEGER;
    oauth2_client_id UUID;
    account_id UUID;
    scope_id BIGINT;
    scope_name VARCHAR(80);
    grant_type_name VARCHAR(50);
    grant_type_id INTEGER;
    authentication_method_name VARCHAR(50);
    authentication_method_id INTEGER;
BEGIN
    SELECT att.id FROM oauth2.access_token_type att
        WHERE att.value = access_token_type
    INTO access_token_type_id;
    IF access_token_type_id IS NULL THEN
        RAISE EXCEPTION 'Non-existent access token type --> %', access_token_type
            USING HINT = 'Please check access token type';
    END IF;
    INSERT INTO account_data.account(name, password, enabled, account_type, password_expires_at)
        VALUES(client_id, bcrypt_client_secret, true, 'client', secret_expires_at)
            RETURNING id INTO account_id;
    INSERT INTO oauth2.client(id, access_token_type_id, name,
        required_authorization_consent, reuse_refresh_tokens,
        required_proof_key, issued_at, redirect_urls,
        post_logout_urls, authorization_code_time_live_millis,
        access_token_time_live_millis, refresh_token_time_live_millis,
        device_code_time_live_millis)
    VALUES (account_id, access_token_type_id, name,
        required_authorization_consent, reuse_refresh_tokens, required_proof_key, issued_at,
        redirect_urls, post_logout_urls,
        authorization_code_time_live_millis, access_token_time_live_millis,
        refresh_token_time_live_millis,
        authorization_code_time_live_millis) RETURNING id INTO oauth2_client_id;
    FOREACH scope_name IN ARRAY scopes LOOP
        SELECT s.id FROM oauth2.scope s WHERE s.name = scope_name INTO scope_id;
        IF scope_id IS NULL THEN
            RAISE EXCEPTION 'Non-existent scope --> %', scope_name
                USING HINT = 'Please check scope names';
        END IF;
        INSERT INTO oauth2.client_scopes(client_id, scope_id)
        VALUES(oauth2_client_id, scope_id);
    END LOOP;
    FOREACH grant_type_name IN ARRAY grant_types LOOP
        SELECT gt.id FROM oauth2.grant_type gt WHERE gt.name = grant_type_name INTO grant_type_id;
        IF grant_type_id IS NULL THEN
            RAISE EXCEPTION 'Non-existent grant-type --> %', grant_type_name
                USING HINT = 'Please check grant-type names';
        END IF;
        INSERT INTO oauth2.client_grant_types(client_id, grant_type_id)
        VALUES(oauth2_client_id, grant_type_id);
    END LOOP;
    FOREACH authentication_method_name IN ARRAY authentication_methods LOOP
        SELECT am.id FROM oauth2.authentication_method am WHERE am.name = authentication_method_name
        INTO authentication_method_id;
        IF authentication_method_id IS NULL THEN
            RAISE EXCEPTION 'Non-existent authentication method name --> %', authentication_method_name
                USING HINT = 'Please check authentication method names';
        END IF;
        INSERT INTO oauth2.client_authentication_methods(client_id, authentication_method_id)
        VALUES(oauth2_client_id, authentication_method_id);
    END LOOP;
END; $$;
