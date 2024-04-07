package ru.jankbyte.spring.oauth2.authorizationserver.configuration;

import org.springframework.http.HttpMethod;
import static org.springframework.http.MediaType.TEXT_HTML;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import static org.springframework.core.Ordered.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.config.Customizer.withDefaults;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.JWKSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.jankbyte.spring.oauth2.authorizationserver.util.RSAUtils;
import ru.jankbyte.spring.oauth2.authorizationserver.property.AuthorizationServerProperty;
import ru.jankbyte.spring.oauth2.authorizationserver.property.AuthorizationServerProperty.JwtProperty;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(
        SecurityConfig.class);

    public static final String[] PERMIT_ALL_MATCHERS = {
        "/error*", "/content/**", SecurityConfig.LOGIN_PAGE + "*"
    };

    public static final String AUTHORIZATION_CONSENT_PAGE = "/oauth2/consent";
    public static final String LOGIN_PAGE = "/login";

    private final AuthorizationServerProperty serverProps;

    public SecurityConfig(AuthorizationServerProperty serverProps) {
        this.serverProps = serverProps;
        JwtProperty jwtProps = serverProps.jwt();
    }

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public SecurityFilterChain oauth2AuthorizationFilterChain(
            HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(oidc -> oidc.clientRegistrationEndpoint(withDefaults()))
            .deviceVerificationEndpoint(endpoint ->
                endpoint.consentPage(AUTHORIZATION_CONSENT_PAGE))
            .authorizationEndpoint(endpoint ->
                endpoint.consentPage(AUTHORIZATION_CONSENT_PAGE));
        return http.exceptionHandling(exception ->
                exception.defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint(LOGIN_PAGE),
                    new MediaTypeRequestMatcher(TEXT_HTML)))
            .oauth2ResourceServer(resourceServer ->
                resourceServer.opaqueToken(withDefaults()))
            .build();
    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http)
            throws Exception {
        String[] matchersUrl = { "/api/**" };
        return http.securityMatcher(matchersUrl)
            .authorizeHttpRequests(request ->
                request.requestMatchers(PERMIT_ALL_MATCHERS).permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/accounts")
                        .hasAuthority("SCOPE_account.create")
                    .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 ->
                oauth2.opaqueToken(withDefaults()))
            .csrf(csrfConfigurer ->
                csrfConfigurer.ignoringRequestMatchers(matchersUrl))
            .sessionManagement(session ->
                session.sessionCreationPolicy(STATELESS))
            .build();
    }

    @Bean
    @Order(LOWEST_PRECEDENCE)
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http)
            throws Exception {
        return http.authorizeHttpRequests(request ->
                request.requestMatchers(PERMIT_ALL_MATCHERS).permitAll()
                    .anyRequest().authenticated())
            .formLogin(formLoginConfigurer ->
                formLoginConfigurer.loginPage(LOGIN_PAGE))
            .build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        String issuerUrl = serverProps.issuerUrl();
        return AuthorizationServerSettings.builder()
            .issuer(issuerUrl)
            .build();
    }

    @Bean 
    public JWKSource<SecurityContext> jwkSource() {
        JWKSet jwkSet = new JWKSet(RSAUtils.generateRsaKeys());
        return new ImmutableJWKSet<>(jwkSet);
    }
}
