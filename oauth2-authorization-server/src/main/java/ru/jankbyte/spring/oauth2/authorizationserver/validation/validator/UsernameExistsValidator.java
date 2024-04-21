package ru.jankbyte.spring.oauth2.authorizationserver.validation.validator;

import ru.jankbyte.spring.oauth2.authorizationserver.validation.annotation.IsUsernameExists;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.AccountRepository;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UsernameExistsValidator
        implements ConstraintValidator<IsUsernameExists, String> {
    private static final Logger log = LoggerFactory.getLogger(
        UsernameExistsValidator.class);
    private final AccountRepository accountRepository;

    public UsernameExistsValidator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean isValid(String username,
            ConstraintValidatorContext context) {
        boolean notExists = !accountRepository.existsByName(username);
        log.debug("Validate username: {} ({})", username, notExists);
        return notExists;
    }
}
