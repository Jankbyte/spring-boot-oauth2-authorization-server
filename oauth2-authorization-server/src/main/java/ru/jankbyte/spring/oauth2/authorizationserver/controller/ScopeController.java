package ru.jankbyte.spring.oauth2.authorizationserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.jankbyte.spring.oauth2.authorizationserver.mapper.ScopeMapper;
import ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2.ScopeInfoResponse;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Scope;
import ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2.ScopeService;

import java.util.Collection;

@RestController
@RequestMapping(path = { "/api/v1/oauth2/scopes" },
    produces = { MediaType.APPLICATION_JSON_VALUE }
)
public class ScopeController {
    private static final Logger log = LoggerFactory.getLogger(
        ScopeController.class);
    private final ScopeService scopeService;
    private final ScopeMapper scopeMapper;

    public ScopeController(ScopeService scopeService,
            ScopeMapper scopeMapper) {
        this.scopeService = scopeService;
        this.scopeMapper = scopeMapper;
    }

    @GetMapping
    public Collection<ScopeInfoResponse> getScopes() {
        Collection<Scope> scopes = scopeService.getAllScopes();
        return scopeMapper.toScopeInfoResponse(scopes);
    }
}
