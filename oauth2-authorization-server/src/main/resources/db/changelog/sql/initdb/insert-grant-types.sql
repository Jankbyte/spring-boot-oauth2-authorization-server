INSERT INTO oauth2.grant_type(name, description)
VALUES('CLIENT_CREDENTIALS', 'Allow to sign in by client credentials (client_id and secret) without form login'),
      ('REFRESH_TOKEN', 'Allow generate refresh token after using authorization code'),
      ('AUTHORIZATION_CODE', 'Allow generate authorization code that could exchange to access token'),
      ('JWT_BEARER', null),
      ('DEVICE_CODE', 'Allow Sign in with proof user code');
