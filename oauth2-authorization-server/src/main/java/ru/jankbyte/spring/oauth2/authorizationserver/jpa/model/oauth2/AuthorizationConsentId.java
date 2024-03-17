package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;

import java.io.Serializable;
import java.util.Objects;

public class AuthorizationConsentId implements Serializable {
    private Client client;
    private Account account;

    private AuthorizationConsentId(Account account, Client client) {
        this.client = client;
        this.account = account;
    }

    public static AuthorizationConsentId from(Account account, Client client) {
        return new AuthorizationConsentId(account, client);
    }

    protected AuthorizationConsentId() {}

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, account);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof AuthorizationConsentId consentId) {
            return Objects.equals(consentId.account, account) &&
                Objects.equals(consentId.client, client);
        }
        return false;
    }
}
