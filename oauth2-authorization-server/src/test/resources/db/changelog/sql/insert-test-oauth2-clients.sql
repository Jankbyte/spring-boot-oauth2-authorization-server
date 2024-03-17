CALL oauth2.create_client(
    'OPAQUE'::VARCHAR,
    'My test OPAQUE client'::VARCHAR,
    'test-opaque-client'::VARCHAR,
    '$2a$10$VzabVzIY66N826PZnaQ/KOSvbZoeIAeWd7/ZhmLvAaTnYa2xebUgG'::VARCHAR,
    false, false, false, now()::TIMESTAMP, null::TIMESTAMP,
    'http://localhost:8080/redirect'::TEXT,
    'http://localhost:8080/logout'::TEXT,
    '{"openid"}'::VARCHAR[],
    '{"REFRESH_TOKEN", "AUTHORIZATION_CODE"}'::VARCHAR[],
    '{"CLIENT_SECRET_BASIC"}'::VARCHAR[]
);
