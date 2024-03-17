package ru.jankbyte.spring.oauth2.authorizationserver.mapper;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Authority;

import java.util.Collection;

@Component
public class AccountMapper {
    public UserDetails toUserDetails(Account account) {
        String[] authorities = account.getAuthorities()
            .stream()
            .map(authority -> authority.getName().name())
            .toArray(String[]::new);
        return User.withUsername(account.getName())
            .authorities(authorities)
            .password(account.getPassword())
            .disabled(!account.isEnabled())
            .build();
    }
}
