package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(schema = "account_data")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private AuthorityName name;
    
    private String description;

    @ManyToMany(mappedBy = "authorities")
    private Set<Account> accounts = new LinkedHashSet<>();

    private Authority(AuthorityName name) {
        this.name = name;
    }

    protected Authority() {}

    public static Authority of(AuthorityName name) {
        return new Authority(name);
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.getAuthorities().add(this);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
        account.getAuthorities().remove(this);
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }

    public AuthorityName getName() {
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
        return Objects.hash(name, id, description);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Authority authority) {
            return Objects.equals(authority.id, id) &&
                Objects.equals(authority.description, description) &&
                Objects.equals(authority.name, name);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Authority[id=%d, name=%s, description=%s]"
            .formatted(id, name, description);
    }

    public enum AuthorityName {
        CREATE_OAUTH2_CLIENT, CREATE_ACCOUNT
    }
}
