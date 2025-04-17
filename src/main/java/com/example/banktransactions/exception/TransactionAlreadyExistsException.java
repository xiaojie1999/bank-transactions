package com.example.banktransactions.exception;

public class TransactionAlreadyExistsException extends RuntimeException {
    public TransactionAlreadyExistsException(String message) {
        super(message);
    }
}