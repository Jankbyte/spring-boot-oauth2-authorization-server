package ru.jankbyte.spring.oauth2.authorizationserver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.jankbyte.spring.oauth2.authorizationserver.repository.AccountRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.exception.repository.EntityNotFoundException;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void saveNewAccount(Account newAccount) {
        accountRepository.save(newAccount);
    }

    @Transactional
    public void saveNewOrUpdateAccount(Account newAccount) {
        String name = newAccount.getName();
        accountRepository.findByName(name).ifPresentOrElse(account -> {
            String password = newAccount.getPassword();
            account.setPassword(password);
            accountRepository.save(account);
        }, () -> accountRepository.save(newAccount));
    }

    public Account getAccountByName(String name) {
        return accountRepository.findByName(name)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Account with name \"%s\" not found".formatted(name)));
    }
}
