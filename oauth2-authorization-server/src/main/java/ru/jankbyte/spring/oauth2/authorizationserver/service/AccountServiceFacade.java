package ru.jankbyte.spring.oauth2.authorizationserver.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;

import java.util.UUID;

@Service
public class AccountServiceFacade {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceFacade(AccountService accountService,
            PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    public void prepareAndSaveAccount(Account account) {
        String password = account.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);
        account.setPassword(encryptedPassword);
        account.setEnabled(true);
        account.setId(UUID.randomUUID());
        accountService.saveNewOrUpdateAccount(account);
    }
}
