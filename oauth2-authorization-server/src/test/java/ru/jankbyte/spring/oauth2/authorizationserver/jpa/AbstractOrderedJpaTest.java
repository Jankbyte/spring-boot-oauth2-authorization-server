package ru.jankbyte.spring.oauth2.authorizationserver.jpa;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import org.springframework.context.annotation.Import;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.jankbyte.spring.oauth2.authorizationserver.TestMain;
import ru.jankbyte.spring.oauth2.authorizationserver.configuration.TestContainersConfiguration;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(TestContainersConfiguration.class)
//@ImportAutoConfiguration(ServiceConnectionAutoConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractOrderedJpaTest {
    @Autowired
    protected EntityManager entityManager;
    
    protected final void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
