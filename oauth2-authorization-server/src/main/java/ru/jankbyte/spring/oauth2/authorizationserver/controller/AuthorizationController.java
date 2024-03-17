package ru.jankbyte.spring.oauth2.authorizationserver.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.http.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2.AuthorizationService;
import ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2.AuthorizationResponse;
import ru.jankbyte.spring.oauth2.authorizationserver.mapper.AuthorizationMapper;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;

@RestController
@RequestMapping(path = {"/api/v1/security/authorization"},
    produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class AuthorizationController {
    private static final Logger log = LoggerFactory.getLogger(
        AuthorizationController.class);

    private final AuthorizationService authorizationService;
    private final AuthorizationMapper authorizationMapper;

    public AuthorizationController(AuthorizationMapper authorizationMapper,
            AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
        this.authorizationMapper = authorizationMapper;
    }

    @GetMapping("/{accountName}")
    @PreAuthorize("#accountName == authentication.name")
    public Iterable<AuthorizationResponse> getAuthorizationsByAccountName(
            @PathVariable String accountName) {
        Collection<Authorization> authorizations =
            authorizationService.getAuthorizationsByAccountName(
                accountName);
        return authorizationMapper.toAuthorizationResponses(authorizations);
    }
}
