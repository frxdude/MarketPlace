package com.diplom.marketplace.handler;

import com.diplom.marketplace.constant.ExceptionType;
import com.diplom.marketplace.dto.response.ErrorResponse;
import com.diplom.marketplace.exception.*;
import com.diplom.marketplace.helper.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RestExceptionHandler
 *
 * @author Sainjargal Ishdorj
 **/

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    Localization localization;

    @Autowired
    public RestExceptionHandler(Localization localization) {
        this.localization = localization;
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + "|" + x.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setError(null);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setValidations(errors);
        response.setType(ExceptionType.VALIDATION.value());

        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);

    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {

        return new ResponseEntity<>(new ErrorResponse(status, ExceptionType.VALIDATION.value(), ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   @NonNull HttpHeaders headers,
                                                                   @NonNull HttpStatus status,
                                                                   @NonNull WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(status, ExceptionType.RUN_TIME.value(), ex.getMessage()), headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {

        return new ResponseEntity<>(new ErrorResponse(status, ExceptionType.VALIDATION.value(), ex.getMessage()), headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {

        return new ResponseEntity<>(new ErrorResponse(status, ExceptionType.VALIDATION.value(), ex.getMessage()), headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {

        return new ResponseEntity<>(new ErrorResponse(status, ExceptionType.RUN_TIME.value(), "JSON Parse error"), headers, status);
    }

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST, "business", ex.getMessage(), ex.errorDetails));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED, "validation", localization.getMessage("error.unauthorized")));
    }

    @ExceptionHandler(value = {RunTimeException.class})
    protected ResponseEntity<Object> handleRunTimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "runtime", localization.getMessage("error.fatal")));
    }

    @ExceptionHandler(value = {RMIException.class})
    protected ResponseEntity<Object> handleRMIException(RMIException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "rmi", localization.getMessage("error.fatal")));
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "fatal", localization.getMessage("error.fatal")));
    }

    @ExceptionHandler(value = {TokenException.class})
    protected ResponseEntity<Object> handleTokenException(TokenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(HttpStatus.FORBIDDEN, "token", localization.getMessage("error.token")));
    }
}
