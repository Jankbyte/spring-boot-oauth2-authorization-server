package ru.jankbyte.spring.oauth2.authorizationserver.controller;

import ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2.AuthorizationConsentServiceFacade;

import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2.ConsentDisplayInfoRequest;
import ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2.ConsentDisplayInfoResponse;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import ru.jankbyte.spring.oauth2.authorizationserver.configuration.SecurityConfig;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthorizationTemplatesController {
    private static final Logger log = LoggerFactory.getLogger(
        AuthorizationTemplatesController.class);
    private final AuthorizationConsentServiceFacade consentService;
    private final AuthorizationServerSettings oauth2ServerSettings;

    public AuthorizationTemplatesController(
            AuthorizationServerSettings oauth2ServerSettings,
            AuthorizationConsentServiceFacade consentService) {
        this.consentService = consentService;
        this.oauth2ServerSettings = oauth2ServerSettings;
    }

    @GetMapping(SecurityConfig.LOGIN_PAGE)
    public String loginPage(Model model) {
        return "login";
    }

    /**
     * The implementation of page for acceptiong authorization consents.
     * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/web/OAuth2AuthorizationEndpointFilter.java">OAuth2AuthorizationEndpointFilter.java</a>
     * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/web/DefaultConsentPage.java">DefaultConsentPage.java</a>
     */
    @GetMapping(SecurityConfig.AUTHORIZATION_CONSENT_PAGE)
    public String consentPage(Model model,
            @RequestParam(OAuth2ParameterNames.CLIENT_ID)
                String clientId,
            @RequestParam String state,
            @RequestParam String scope,
            @RequestParam(name = OAuth2ParameterNames.USER_CODE,
                required = false) String userCode,
            @AuthenticationPrincipal UserDetails principal) {
        String principalName = principal.getUsername();
        Collection<String> requestedScopes =
            consentService.convertScopeParamToCollection(scope);
        ConsentDisplayInfoRequest consentDisplayRequest =
            new ConsentDisplayInfoRequest(clientId, principalName,
                requestedScopes);
        ConsentDisplayInfoResponse response =
            consentService.getConsentDisplayInfoResponse(
                consentDisplayRequest);
        model.addAttribute("scopesToAuthorize",
            response.scopesToAuthorize());
        model.addAttribute("scopesPreviouslyAuthorized",
            response.scopesPreviouslyAuthorized());
        model.addAttribute("clientName", response.clientName());
        model.addAttribute("clientLogoUrl", response.clientLogoUrl());
        model.addAttribute("userCode", userCode);
        String postUri = userCode != null ?
            oauth2ServerSettings.getDeviceVerificationEndpoint() :
            oauth2ServerSettings.getAuthorizationEndpoint();
        model.addAttribute("postUri", postUri);
        return "authorization-consents";
    }
}
