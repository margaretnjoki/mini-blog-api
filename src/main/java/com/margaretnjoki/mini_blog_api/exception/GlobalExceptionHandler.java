package com.margaretnjoki.mini_blog_api.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import static ch.qos.logback.classic.spi.ThrowableProxyVO.build;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(ResourceNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> fields.put(e.getField(), e.getDefaultMessage()));
        return Map.of("error", "validation failed", "fields", fields);
    }

    @ExceptionHandler(ConstraintViolationException.class)   // @RequestParam validation
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleConstraint(ConstraintViolationException ex) {
        return Map.of("error", "validation failed", "message", ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBadRequest(BadRequestException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleEmailInUse(EmailAlreadyInUseException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleBadCredentials(BadCredentialsException ex) {
        return Map.of("error", "invalid email or password");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleAccessDenied(AccessDeniedException ex) {
        return Map.of("error", "you do not have permission to perform this action");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAny(Exception ex) {
        log.error("Unexpected error", ex);   // full stack trace goes to the LOG, never to the client
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "unexpected error", "message", ex.getMessage()));
    }
}

