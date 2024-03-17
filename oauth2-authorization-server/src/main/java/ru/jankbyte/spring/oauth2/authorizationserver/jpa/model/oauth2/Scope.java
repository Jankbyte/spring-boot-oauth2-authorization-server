package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityQueryNames;

@NamedQueries({
    @NamedQuery(
        name = EntityQueryNames.SCOPE_COUNT_BY_NAMES,
        query = "SELECT COUNT(s) FROM Scope s WHERE s.name IN (:scopeNames)"
    )
})
@Entity
@Table(schema = "oauth2")
public class Scope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "scopes")
    private Set<Client> clients = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "scopes")
    private Set<AuthorizationConsent> authorizationConsents = new LinkedHashSet<>();

    protected Scope() {}

    private Scope(String name) {
        this.name = name;
    }

    public static Scope of(String name) {
        return new Scope(name);
    }

    public Set<AuthorizationConsent> getAuthorizationConsents() {
        return authorizationConsents;
    }

    public void addAuthorizationConsent(AuthorizationConsent authorizationConsent) {
        authorizationConsents.add(authorizationConsent);
        authorizationConsent.getScopes().add(this);
    }

    public void removeAuthorizationConsent(AuthorizationConsent authorizationConsent) {
        authorizationConsents.remove(authorizationConsent);
        authorizationConsent.getScopes().remove(this);
    }

    public void addClient(Client client) {
        clients.add(client);
        client.getScopes().add(this);
    }

    public void removeClient(Client client) {
        clients.remove(client);
        client.getScopes().remove(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Client> getClients() {
        return clients;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Scope scope) {
            return Objects.equals(scope.id, id) &&
                Objects.equals(scope.description, description) &&
                Objects.equals(scope.name, name);
        }
        return false;
    }

    @Override
    public String toString() {
        String name = getClass().getSimpleName();
        return "%s[id=%d, name=%s, description=%s]"
            .formatted(name, id, this.name, description);
    }
}
