package ru.jankbyte.spring.oauth2.authorizationserver.component.factory.problemdetail;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ProblemDetail;
import static org.springframework.http.HttpStatus.CONFLICT;
import org.springframework.validation.ObjectError;

import java.util.Collection;

/**
 * An implementation for generating problem details from {@link org.springframework.web.bind.MethodArgumentNotValidException}.
 */
public final class ValidationProblemDetailFactory
        implements ProblemDetailFactory<MethodArgumentNotValidException> {
    @Override
    public ProblemDetail createProblemDetailByException(
            MethodArgumentNotValidException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(CONFLICT,
            "Error while validating some of props");
        detail.setTitle("Validation error");
        Collection<String> errors = ex.getAllErrors().stream()
            .map(ObjectError::getDefaultMessage)
            .toList();
        detail.setProperty("errors", errors);
        return detail;
    }
}
