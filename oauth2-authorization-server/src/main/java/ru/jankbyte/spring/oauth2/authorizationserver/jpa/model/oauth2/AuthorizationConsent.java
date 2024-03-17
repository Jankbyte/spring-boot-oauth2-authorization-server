package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2;

import jakarta.persistence.*;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityGraphNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@IdClass(AuthorizationConsentId.class)
@Table(schema = "oauth2", name = "authorization_consent")
@NamedEntityGraph(
    name = EntityGraphNames.AUTHORIZATION_CONSENT_LOAD_SCOPES,
    attributeNodes = {
        @NamedAttributeNode("scopes")
    }
)
public class AuthorizationConsent {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany
    @JoinTable(schema = "oauth2",
        name = "authorization_consent_scopes",
        joinColumns = {
            @JoinColumn(name = "client_id",
                referencedColumnName = "client_id"),
            @JoinColumn(name = "account_id",
                referencedColumnName = "account_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "scope_id")
        }
    )
    private Set<Scope> scopes = new LinkedHashSet<>();

    protected AuthorizationConsent() {}

    public static ConsentBuilder builder() {
        return new ConsentBuilder();
    }

    public void addScope(Scope scope) {
        scopes.add(scope);
        scope.getAuthorizationConsents().add(this);
    }

    public void removeScope(Scope scope) {
        scopes.remove(scope);
        scope.getAuthorizationConsents().remove(this);
    }

    public Set<Scope> getScopes() {
        return scopes;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public static final class ConsentBuilder {
        private final AuthorizationConsent authorizationConsent =
            new AuthorizationConsent();

        public ConsentBuilder account(Account account) {
            authorizationConsent.setAccount(account);
            return this;
        }

        public ConsentBuilder client(Client client) {
            authorizationConsent.setClient(client);
            return this;
        }

        public ConsentBuilder scopes(Collection<Scope> scopes) {
            scopes.forEach(authorizationConsent::addScope);
            return this;
        }

        public AuthorizationConsent build() {
            return authorizationConsent;
        }
    }
}
