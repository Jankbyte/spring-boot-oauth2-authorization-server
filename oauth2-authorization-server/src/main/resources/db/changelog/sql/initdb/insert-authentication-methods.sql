INSERT INTO oauth2.authentication_method(name, description)
VALUES('CLIENT_SECRET_BASIC', 'Allow basic-authentication for client (Authorization: Basic <base64-encoded-cred>)'),
      ('CLIENT_SECRET_POST', 'Allow form-post authentication'),
      ('CLIENT_SECRET_JWT', null),
      ('PRIVATE_KEY_JWT', null),
      ('NONE', 'Allow authentication without client secret (only access token will generated)');
