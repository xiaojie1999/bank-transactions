package com.example.banktransactions.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Setter
@Getter
public class Transaction {

    // Getters and Setters
    private String id;

    @NotBlank(message = "Account ID cannot be blank")
    private String accountId;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Transaction type cannot be blank")
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER

    private String description;

    private LocalDateTime timestamp;

    // Constructors
    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String accountId, BigDecimal amount, String type, String description) {
        this();
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}