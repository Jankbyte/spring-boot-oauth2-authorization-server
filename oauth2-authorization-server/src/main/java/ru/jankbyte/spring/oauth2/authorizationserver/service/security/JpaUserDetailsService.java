package ru.jankbyte.spring.oauth2.authorizationserver.service.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.jankbyte.spring.oauth2.authorizationserver.mapper.AccountMapper;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.AccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private static final Logger log = LoggerFactory.getLogger(
        JpaUserDetailsService.class);

    public JpaUserDetailsService(
            AccountRepository accountRepository,
            AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserDetails details = accountRepository.findByName(username)
            .map(accountMapper::toUserDetails)
            .orElseThrow(() -> {
                String message = "User with name=%s not found"
                    .formatted(username);
                return new UsernameNotFoundException(message);
            });
        if (log.isDebugEnabled()) {
            log.debug("Details: {}", details);
        }
        return details;
    }
}
