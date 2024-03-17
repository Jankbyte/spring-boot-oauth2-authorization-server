package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(schema = "oauth2", name = "authentication_method")
public class AuthenticationMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AuthenticationMethodName name;

    private String description;

    @ManyToMany(mappedBy = "authenticationMethods")
    private Set<Client> clients = new LinkedHashSet<>();

    protected AuthenticationMethod() {}

    private AuthenticationMethod(AuthenticationMethodName name) {
        this.name = name;
    }

    public static AuthenticationMethod of(AuthenticationMethodName name) {
        return new AuthenticationMethod(name);
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        clients.add(client);
        client.getAuthenticationMethods().add(this);
    }

    public void removeClient(Client client) {
        clients.remove(client);
        client.getAuthenticationMethods().remove(this);
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(AuthenticationMethodName name) {
        this.name = name;
    }

    public AuthenticationMethodName getName() {
        return name;
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
        if (object instanceof AuthenticationMethod method) {
            return Objects.equals(method.id, id) &&
                Objects.equals(method.description, description) &&
                Objects.equals(method.name, name);
        }
        return false;
    }

    @Override
    public String toString() {
        return "AuthenticationMethod[id=%d, name=%s, description=%s]"
            .formatted(id, name, description);
    }

    public enum AuthenticationMethodName {
        CLIENT_SECRET_BASIC, CLIENT_SECRET_POST,
        CLIENT_SECRET_JWT, PRIVATE_KEY_JWT, NONE
    }
}
