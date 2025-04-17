package com.example.banktransactions.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {TransactionAlreadyExistsException.class})
    protected ResponseEntity<Object> handleTransactionAlreadyExists(
            TransactionAlreadyExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {TransactionNotFoundException.class})
    protected ResponseEntity<Object> handleTransactionNotFound(
            TransactionNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}