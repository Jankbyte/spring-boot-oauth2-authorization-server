package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(schema = "oauth2", name = "grant_type")
public class GrantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private GrantTypeName name;
    
    private String description;

    @ManyToMany(mappedBy = "grantTypes")
    private Set<Client> clients = new LinkedHashSet<>();

    public static GrantType of(GrantTypeName name) {
        return new GrantType(name);
    }

    protected GrantType() {}

    private GrantType(GrantTypeName name) {
        this.name = name;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        clients.add(client);
        client.getGrantTypes().add(this);
    }

    public void removeGrantType(Client client) {
        clients.remove(client);
        client.getGrantTypes().remove(this);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(GrantTypeName name) {
        this.name = name;
    }

    public GrantTypeName getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
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
        if (object instanceof GrantType grantType) {
            return Objects.equals(grantType.id, id) &&
                Objects.equals(grantType.description, description) &&
                Objects.equals(grantType.name, name);
        }
        return false;
    }

    @Override
    public String toString() {
        return "GrantType[id=%d, name=%s, description=%s]"
            .formatted(id, name, description);
    }

    public enum GrantTypeName {
        CLIENT_CREDENTIALS, REFRESH_TOKEN, AUTHORIZATION_CODE,
        JWT_BEARER, DEVICE_CODE
    }
}
