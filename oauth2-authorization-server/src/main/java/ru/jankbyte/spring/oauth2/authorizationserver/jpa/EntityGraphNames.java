package ru.jankbyte.spring.oauth2.authorizationserver.jpa;

public final class EntityGraphNames {
    private EntityGraphNames() {}

    public static final String AUTHORIZATION_FIND_BY_TOKEN =
        "Authorization.graph.findByToken";
    public static final String CLIENT_FIND_BY_ID = "Client.graph.findById";
    public static final String ACCOUNT_FIND_BY_NAME = "Account.graph.findByName";
    public static final String AUTHORIZATION_CONSENT_LOAD_SCOPES =
        "AuthorizationConsent.graph.loadScopes";
}
