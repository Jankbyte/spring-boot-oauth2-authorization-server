package ru.jankbyte.spring.oauth2.authorizationserver.component.factory.problemdetail;

import org.springframework.http.ProblemDetail;

/**
 * An abstract factory for creating {@link org.springframework.http.ProblemDetail} by {@link java.lang.Exception}
 * type.
 * @see org.springframework.http.ProblemDetail
 * @since 1.0
 */
public interface ProblemDetailFactory<T extends Exception> {
    /**
     * Create problem detail by exception type.
     * @param ex The exception for creating {@link org.springframework.http.ProblemDetail}
     * @return {@link org.springframework.http.ProblemDetail} with generated information by {@code ex}
     */
    ProblemDetail createProblemDetailByException(T ex);
}
