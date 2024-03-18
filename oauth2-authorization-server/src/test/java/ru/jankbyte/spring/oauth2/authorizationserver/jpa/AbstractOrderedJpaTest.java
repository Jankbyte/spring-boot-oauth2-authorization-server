package ru.jankbyte.spring.oauth2.authorizationserver.jpa;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import ru.jankbyte.spring.oauth2.authorizationserver.configuration.TestContainersConfiguration;

@DataJpaTest
@Import(TestContainersConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractOrderedJpaTest {
    @Autowired
    protected EntityManager entityManager;
    
    protected final void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
