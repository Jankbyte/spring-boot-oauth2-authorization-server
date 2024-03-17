package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2;

import jakarta.persistence.*;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;

@Entity
@DiscriminatorValue("client")
public class ClientAccount extends Account {
    public static AccountBuilder<ClientAccount> clientAccountBuilder() {
        return new AccountBuilder<>(new ClientAccount());
    }
}
