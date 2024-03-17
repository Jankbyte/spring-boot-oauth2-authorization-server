package ru.jankbyte.spring.oauth2.authorizationserver.component;

import java.util.List;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.context.annotation.Import;

@TestComponent
@Import({
    TestClientsCreator.class,
    TestAccountsCreator.class
})
public class DatabaseCreatorsExecutor {
    private final List<DatabaseCreator> creators;

    public DatabaseCreatorsExecutor(List<DatabaseCreator> creators) {
        this.creators = creators;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void executeCreators() {
        creators.forEach(DatabaseCreator::create);
    }
}
