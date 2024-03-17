package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

import ru.jankbyte.spring.oauth2.authorizationserver.service.security.oauth2.JpaOAuth2AuthorizationService;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.ClientRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AuthorizationRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.service.AccountService;
import ru.jankbyte.spring.oauth2.authorizationserver.mapper.AuthorizationMapper;
import ru.jankbyte.spring.oauth2.authorizationserver.mapper.ClientMapper;

@ExtendWith(MockitoExtension.class)
public class JpaOAuth2AuthorizationServiceTest {
    private JpaOAuth2AuthorizationService service;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AuthorizationRepository authorizationRepository;

    @Mock
    private AccountService accountService;

    //@Spy
    //private AuthorizationMapper mapper = new AuthorizationMapper(
    //    new ClientMapper());

    @BeforeEach
    public void initialize() {
        //service = new JpaOAuth2AuthorizationService(clientRepository,
        //    authorizationRepository, accountService, mapper);
    }
}
