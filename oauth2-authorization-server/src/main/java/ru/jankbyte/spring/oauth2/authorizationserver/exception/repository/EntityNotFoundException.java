package ru.jankbyte.spring.oauth2.authorizationserver.exception.repository;

import ru.jankbyte.spring.oauth2.authorizationserver.exception.ApplicationException;

public class EntityNotFoundException extends RepositoryLayerException {
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
