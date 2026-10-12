package co.edu.cinematch.api.exception;

import co.edu.cinematch.core.exception.AuthenticationException;
import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail notFound(ResourceNotFoundException error) { return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, error.getMessage()); }
    @ExceptionHandler(ValidationException.class)
    ProblemDetail badRequest(ValidationException error) { return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, error.getMessage()); }
    @ExceptionHandler(AuthenticationException.class)
    ProblemDetail unauthorized(AuthenticationException error) { return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, error.getMessage()); }
}

