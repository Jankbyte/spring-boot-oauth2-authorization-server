package ru.jankbyte.spring.oauth2.authorizationserver.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import ru.jankbyte.spring.oauth2.authorizationserver.component.factory.problemdetail.ProblemDetailFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice(assignableTypes = {
    AuthorizationController.class
})
public class ProblemHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(
        ProblemHandler.class);

    private final ProblemDetailFactory<Exception> problemDetailFactory;

    public ProblemHandler(
            ProblemDetailFactory<Exception> problemDetailFactory) {
        this.problemDetailFactory = problemDetailFactory;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        log.debug("Handling MethodArgumentNotValidException for path: {}",
            request.getContextPath());
        ProblemDetail details =
            problemDetailFactory.createProblemDetailByException(ex);
        HttpStatusCode code = HttpStatus.valueOf(details.getStatus());
        return handleExceptionInternal(ex, details, headers, code, request);
    }
}
