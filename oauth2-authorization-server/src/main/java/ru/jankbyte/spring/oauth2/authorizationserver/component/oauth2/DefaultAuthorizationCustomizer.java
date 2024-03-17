package ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Builder;
import org.springframework.stereotype.Component;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DefaultAuthorizationCustomizer implements AuthorizationCustomizer {
    private static final Logger log = LoggerFactory.getLogger(
        DefaultAuthorizationCustomizer.class);

    @Override
    public void customize(Builder builder, Authorization auth) {
        log.debug("Customizing authorization");
        Account account = auth.getAccount();
        String id = account.getId().toString();
        builder.attribute(AttributeNames.ACCOUNT_ID, id);
    }

    public static final class AttributeNames {
        public static final String ACCOUNT_ID = "accountId";

        private AttributeNames() {}
    }
}
