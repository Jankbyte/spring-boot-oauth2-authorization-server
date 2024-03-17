package ru.jankbyte.spring.oauth2.authorizationserver.jpa;

public final class EntityQueryNames {
    private EntityQueryNames() {}

    public static final String AUTHORIZATION_FIND_BY_UNKNOWN_TOKEN =
        "Authorization.query.findByUnknownTokenType";
    public static final String AUTHORIZATION_DELETE_EXPIRED =
        "Authorization.query.deleteExpired";
    public static final String AUTHORIZATION_FIND_BY_ACCOUNT_NAME_AND_NON_EXPIRED =
        "Authorization.query.findByAccountNameAndNonExpired";
    public static final String SCOPE_COUNT_BY_NAMES = "Scope.query.countByNames";
}
