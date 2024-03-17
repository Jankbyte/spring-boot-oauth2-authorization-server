INSERT INTO oauth2.access_token_type(value, description)
    VALUES('OPAQUE', 'Using for getting user-state from authorization server'),
          ('JWT', 'Contains user-state inside token');