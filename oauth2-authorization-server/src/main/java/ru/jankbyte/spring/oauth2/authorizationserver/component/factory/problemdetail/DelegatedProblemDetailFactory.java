package ru.jankbyte.spring.oauth2.authorizationserver.component.factory.problemdetail;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation that encapsulate other factories for creating {@link org.springframework.http.ProblemDetail}.
 * <p>That's a primary implementation for using.</p>
 */
@Component
public class DelegatedProblemDetailFactory
        implements ProblemDetailFactory<Exception> {
    private final Map<Class<? extends Exception>,
        ProblemDetailFactory<? extends Exception>> factories =
            new HashMap<>();

    public DelegatedProblemDetailFactory() {
        factories.put(MethodArgumentNotValidException.class,
            new ValidationProblemDetailFactory());
    }

    @Override
    public ProblemDetail createProblemDetailByException(Exception ex) {
        Class<? extends Exception> key = ex.getClass();
        @SuppressWarnings("unchecked")
        ProblemDetailFactory<Exception> factory =
            (ProblemDetailFactory<Exception>) factories.get(key);
        return factory.createProblemDetailByException(ex);
    }
}
