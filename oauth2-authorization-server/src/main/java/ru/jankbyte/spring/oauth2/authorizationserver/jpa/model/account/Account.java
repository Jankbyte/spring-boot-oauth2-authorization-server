package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account;

import jakarta.persistence.*;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityGraphNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthorizationConsent;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.Objects;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Collection;

@NamedEntityGraph(
    name = EntityGraphNames.ACCOUNT_FIND_BY_NAME,
    attributeNodes = {
        @NamedAttributeNode("authorities")
    }
)
@Entity
@Inheritance
@Table(schema = "account_data")
@DiscriminatorValue("customer")
@DiscriminatorColumn(name = "account_type", length = 10)
public class Account {
    @Id
    private UUID id;

    @Column(name = "password_expires_at", columnDefinition = "TIMESTAMP")
    private OffsetDateTime passwordExpiresAt;

    @Column(unique = true, nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToMany
    @JoinTable(schema = "account_data", name = "account_authorities",
        joinColumns = {
            @JoinColumn(name = "account_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "authority_id")
        }
    )
    private Set<Authority> authorities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,
        orphanRemoval = true)
    private Set<Authorization> authorizations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,
        orphanRemoval = true)
    private Set<AuthorizationConsent> authorizationConsents = new LinkedHashSet<>();

    public static AccountBuilder<Account> builder() {
        return new AccountBuilder<>(new Account());
    }

    protected Account() {}

    public void setPasswordExpiresAt(OffsetDateTime passwordExpiresAt) {
        this.passwordExpiresAt = passwordExpiresAt;
    }

    public OffsetDateTime getPasswordExpiresAt() {
        return passwordExpiresAt;
    }

    public void addAuthorizationConsent(AuthorizationConsent authorizationConsent) {
        authorizationConsents.add(authorizationConsent);
        authorizationConsent.setAccount(this);
    }

    public void removeAuthorizationConsent(AuthorizationConsent authorizationConsent) {
        authorizationConsents.remove(authorizationConsent);
        authorizationConsent.setAccount(null);
    }
    
    public Set<AuthorizationConsent> getAuthorizationConsents() {
        return authorizationConsents;
    }

    public void addAuthorization(Authorization authorization) {
        authorizations.add(authorization);
        authorization.setAccount(this);
    }

    public void removeAuthorization(Authorization authorization) {
        authorizations.remove(authorization);
        authorization.setAccount(null);
    }

    public Set<Authorization> getAuthorizations() {
        return authorizations;
    }

    public void addAuthority(Authority authority) {
        authorities.add(authority);
        authority.getAccounts().add(this);
    }

    public void removeAuthority(Authority authority) {
        authorities.remove(authority);
        authority.getAccounts().remove(this);
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, enabled, passwordExpiresAt);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Account account) {
            return Objects.equals(account.id, id) &&
                Objects.equals(account.name, name) &&
                Objects.equals(account.passwordExpiresAt, passwordExpiresAt) &&
                Objects.equals(account.password, password) &&
                Objects.equals(account.enabled, enabled);
        }
        return false;
    }

    @Override
    public String toString() {
        String passwordDescription = password != null ? "[PROTECTED]" : null;
        return """
            Account[id=%s, name=%s, password=%s, \
            enabled=%s, passwordExpiresAt=%s] \
            """
            .formatted(id, name, passwordDescription,
                enabled, passwordExpiresAt);
    }

    public static class AccountBuilder<T extends Account> {
        private final T account;

        public AccountBuilder(T account) {
            this.account = account;
        }

        public AccountBuilder<T> id(UUID id) {
            account.setId(id);
            return this;
        }

        public AccountBuilder<T> passwordExpiresAt(
                OffsetDateTime passwordExpiresAt) {
            account.setPasswordExpiresAt(passwordExpiresAt);
            return this;
        }

        public AccountBuilder<T> name(String name) {
            account.setName(name);
            return this;
        }

        public AccountBuilder<T> password(String password) {
            account.setPassword(password);
            return this;
        }

        public AccountBuilder<T> enabled(boolean enabled) {
            account.setEnabled(enabled);
            return this;
        }

        public AccountBuilder<T> authorities(
                Collection<Authority> authorities) {
            authorities.forEach(account::addAuthority);
            return this;
        }

        public T build() {
            return account;
        }
    }
}
