package ru.jankbyte.spring.oauth2.authorizationserver.exception;

public abstract class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(Throwable throwable) {
        super(throwable);
    }
}
