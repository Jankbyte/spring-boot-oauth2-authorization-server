package ru.jankbyte.spring.oauth2.authorizationserver.exception.repository;

import ru.jankbyte.spring.oauth2.authorizationserver.exception.ApplicationException;

public abstract class RepositoryLayerException extends ApplicationException {
    public RepositoryLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryLayerException(String message) {
        super(message);
    }

    public RepositoryLayerException(Throwable throwable) {
        super(throwable);
    }
}
