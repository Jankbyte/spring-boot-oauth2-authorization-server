package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represent the type of access token as entity.
 */
@Entity
@Table(schema = "oauth2", name = "access_token_type")
public class AccessTokenType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TokenTypeValue value;

    private String description;

    @OneToMany(mappedBy = "accessTokenType", orphanRemoval = true,
        cascade = CascadeType.ALL)
    private Set<Client> clients = new LinkedHashSet<>();

    protected AccessTokenType() {}

    private AccessTokenType(TokenTypeValue value) {
        this.value = value;
    }

    public static AccessTokenType of(TokenTypeValue value) {
        return new AccessTokenType(value);
    }
    
    public Set<Client> getClients() {
        return clients;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void addClient(Client client) {
        clients.add(client);
        client.setAccessTokenType(this);
    }

    public void removeClient(Client client) {
        clients.remove(client);
        client.setAccessTokenType(null);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public TokenTypeValue getValue() {
        return value;
    }

    public void setValue(TokenTypeValue value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, description);
    }

    @Override
    public String toString() {
        return "AccessTokenType[id=%d, value=%s, description=%s]"
            .formatted(id, value, description);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof AccessTokenType tokenType) {
            return Objects.equals(tokenType.id, id) &&
                Objects.equals(tokenType.description, description) &&
                Objects.equals(tokenType.value, value);
        }
        return false;
    }

    public enum TokenTypeValue {
        OPAQUE, JWT
    }
}
