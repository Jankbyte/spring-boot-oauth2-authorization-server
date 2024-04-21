CALL oauth2.create_client(
    'OPAQUE'::VARCHAR,
    'Default OAuth2 client'::VARCHAR,
    'duke'::VARCHAR,
    '$2a$10$NPvyF.c.2EkUA3H2qZSCNe/OSX9GONA10ePnucDpRopif09kndtAy'::VARCHAR,
    true, false, false, now()::TIMESTAMP, null::TIMESTAMP,
    'http://localhost:8080/redirect'::TEXT,
    'http://localhost:8080/logout'::TEXT,
    '{"client.create", "openid", "profile"}'::VARCHAR[],
    '{"CLIENT_CREDENTIALS", "REFRESH_TOKEN", "AUTHORIZATION_CODE"}'::VARCHAR[],
    '{"CLIENT_SECRET_POST", "CLIENT_SECRET_BASIC"}'::VARCHAR[]
);
