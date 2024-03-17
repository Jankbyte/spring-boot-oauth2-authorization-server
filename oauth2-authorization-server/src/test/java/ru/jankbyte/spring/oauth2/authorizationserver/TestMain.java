package ru.jankbyte.spring.oauth2.authorizationserver;

import ru.jankbyte.spring.oauth2.authorizationserver.configuration.TestContainersConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class TestMain {
    public static void main(String[] args) {
        SpringApplication.from(Main::main)
            .with(TestContainersConfiguration.class)
            .run(args);
    }
}
