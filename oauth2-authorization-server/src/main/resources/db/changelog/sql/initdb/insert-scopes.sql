INSERT INTO oauth2.scope(name, description)
    VALUES('client.create', 'Create new OAuth2 clients with OpenID'),
          ('scope.create', 'Allows create new OAuth2 scopes for authorization server'),
          ('account.create', 'Create new customer-accounts'),
          ('openid', 'The Open ID connection enabling'),
          ('profile', 'Getting information about user');
