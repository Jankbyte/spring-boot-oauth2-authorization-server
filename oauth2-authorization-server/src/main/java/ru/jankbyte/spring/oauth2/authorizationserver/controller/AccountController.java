package ru.jankbyte.spring.oauth2.authorizationserver.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;


import ru.jankbyte.spring.oauth2.authorizationserver.mapper.AccountMapper;
import ru.jankbyte.spring.oauth2.authorizationserver.service.AccountServiceFacade;
import ru.jankbyte.spring.oauth2.authorizationserver.dto.account.CreateAccountRequest;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = { "/api/v1/accounts" },
    produces = { MediaType.APPLICATION_JSON_VALUE }
)
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(
        AccountController.class);

    private final AccountServiceFacade accountServiceFacade;
    private final AccountMapper accountMapper;

    public AccountController(AccountServiceFacade accountServiceFacade,
            AccountMapper accountMapper) {
        this.accountServiceFacade = accountServiceFacade;
        this.accountMapper = accountMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createNewAccount(
            @RequestBody @Valid CreateAccountRequest createAccountRequest) {
        Account account = accountMapper.toAccount(createAccountRequest);
        accountServiceFacade.prepareAndSaveAccount(account);
    }
}
