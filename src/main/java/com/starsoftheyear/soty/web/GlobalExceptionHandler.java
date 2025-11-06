package com.starsoftheyear.soty.web;

import com.starsoftheyear.soty.api.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.starsoftheyear.soty")
public class GlobalExceptionHandler {

    // --- helpers ---
    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message,
                                                String path, Map<String,String> validation) {
        var body = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                validation == null || validation.isEmpty() ? null : validation
        );
        return ResponseEntity.status(status).body(body);
    }

    // 1) Things I already throw (ResponseStatusException etc.)
    @ExceptionHandler({ ResponseStatusException.class, ErrorResponseException.class })
    public ResponseEntity<ErrorResponse> onResponseStatus(ResponseStatusException ex,
                                                          HttpServletRequest req) {
        var status = HttpStatus.valueOf(ex.getStatusCode().value());
        return build(status, Optional.ofNullable(ex.getReason()).orElse(status.getReasonPhrase()),
                req.getRequestURI(), null);
    }

    // 2) Validation: @Valid on @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> onMethodArgNotValid(MethodArgumentNotValidException ex,
                                                             HttpServletRequest req) {
        Map<String,String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a,b) -> a,
                        LinkedHashMap::new
                ));
        return build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), errors);
    }

    // 3) Validation: @Validated on params/path vars
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> onConstraintViolation(ConstraintViolationException ex,
                                                               HttpServletRequest req) {
        Map<String,String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        jakarta.validation.ConstraintViolation::getMessage,
                        (a,b) -> a,
                        LinkedHashMap::new
                ));
        return build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), errors);
    }

    // 4) Bad JSON / wrong types / missing params
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> onBadRequest(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
    }

    // 5) Not found variants
    @ExceptionHandler({ EntityNotFoundException.class, NoSuchElementException.class })
    public ResponseEntity<ErrorResponse> onNotFound(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), null);
    }

    // 6) DB conflicts (unique key, FK, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> onDataIntegrity(DataIntegrityViolationException ex,
                                                         HttpServletRequest req) {
        String detail = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse(ex.getMessage());
        return build(HttpStatus.CONFLICT, detail, req.getRequestURI(), null);
    }

    // 7) Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> onAny(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getRequestURI(), null);
    }
}
