package ru.jankbyte.spring.oauth2.authorizationserver.service.security.oauth2;

import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.*;

import ru.jankbyte.spring.oauth2.authorizationserver.mapper.AuthorizationMapper;
import ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2.AuthorizationService;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AuthorizationRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;
import ru.jankbyte.spring.oauth2.authorizationserver.util.ToStringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.Set;
import java.util.Map;
import static java.time.ZoneOffset.UTC;

@Service
public class JpaOAuth2AuthorizationService
        implements OAuth2AuthorizationService {
    private static final Logger log = LoggerFactory.getLogger(
        JpaOAuth2AuthorizationService.class);

    private final AuthorizationMapper authorizationMapper;
    private final AuthorizationRepository authorizationRepository;
    private final AuthorizationService authorizationService;

    public JpaOAuth2AuthorizationService(
            AuthorizationRepository authorizationRepository,
            AuthorizationMapper authorizationMapper,
            AuthorizationService authorizationService) {
        this.authorizationMapper = authorizationMapper;
        this.authorizationRepository = authorizationRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public void save(OAuth2Authorization oauth2Authorization) {
        printAuthorizationInfo("Save authorization", oauth2Authorization);
        Authorization authorization = authorizationMapper.toAuthorization(
            oauth2Authorization);
        authorizationService.saveOrUpdateAuthorization(authorization);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        printAuthorizationInfo("Removed authorization", authorization);
        String id = authorization.getId();
        authorizationService.removeAuthorizationById(id);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        log.debug("Find authorization by ID: {}", id);
        OAuth2Authorization oauth2Authorization =
            authorizationService.getAuthorizationById(id)
                .map(authorizationMapper::toOAuth2Authorization)
                .orElse(null);
        printAuthorizationInfo("Found authorization", oauth2Authorization);
        return oauth2Authorization;
    }

    @Override
    public OAuth2Authorization findByToken(String token,
            OAuth2TokenType tokenType) {
        String tokenTypeValue = tokenType != null ?
            tokenType.getValue() : null;
        log.debug("Find authorization by token: {} ({})",
            token, tokenTypeValue);
        OAuth2Authorization oauth2Authorization = null;
        if (tokenType == null) {
            oauth2Authorization = authorizationRepository.findByUnknownToken(token)
                .map(authorizationMapper::toOAuth2Authorization)
                .orElse(null);
        } else if (OAuth2ParameterNames.STATE.equals(tokenTypeValue)) {
            oauth2Authorization = authorizationRepository.findByState(token)
                .map(authorizationMapper::toOAuth2Authorization)
                .orElse(null);
        } else if (OAuth2ParameterNames.CODE.equals(tokenTypeValue)) {
            oauth2Authorization = authorizationRepository.findByAuthorizationCodeValue(token)
                .map(authorizationMapper::toOAuth2Authorization)
                .orElse(null);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            oauth2Authorization = authorizationRepository.findByAccessTokenValue(token)
                .map(authorizationMapper::toOAuth2Authorization)
                .orElse(null);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenTypeValue)) {
            oauth2Authorization = authorizationRepository.findByOidcTokenValue(token)
                .map(authorizationMapper::toOAuth2Authorization)
                .orElse(null);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            oauth2Authorization = authorizationRepository.findByRefreshTokenValue(token)
                .map(authorizationMapper::toOAuth2Authorization)
                .orElse(null);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenTypeValue)) {
            oauth2Authorization = authorizationRepository.findByUserCodeValue(token)
                .map(authorizationMapper::toOAuth2Authorization)
                .orElse(null);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenTypeValue)) {
            oauth2Authorization = authorizationRepository.findByDeviceCodeValue(token)
                .map(authorizationMapper::toOAuth2Authorization)
                .orElse(null);
        }
        printAuthorizationInfo("Found authorization", oauth2Authorization);
        return oauth2Authorization;
    }

    private void printAuthorizationInfo(String message,
            OAuth2Authorization oauth2Authorization) {
        if (log.isDebugEnabled()) {
            String authorizationAsString = ToStringUtils.toString(
                oauth2Authorization);
            log.debug("{}: {}", message, authorizationAsString);
        }
    }
}
