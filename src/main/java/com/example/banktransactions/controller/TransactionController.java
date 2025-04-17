package com.example.banktransactions.controller;

import com.example.banktransactions.model.Transaction;
import com.example.banktransactions.service.TransactionService;
import com.example.banktransactions.exception.TransactionAlreadyExistsException;
import com.example.banktransactions.exception.TransactionNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/transactions")
@Validated
@Tag(name = "Transaction Management API", description = "API for managing bank transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Create a new transaction",
            description = "Creates a new transaction with unique ID",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Transaction created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid transaction data",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Transaction already exists",
                            content = @Content
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        } catch (TransactionAlreadyExistsException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Operation(
            summary = "Get transaction by ID",
            description = "Retrieves a transaction by its unique ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transaction found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transaction not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            return ResponseEntity.ok(transaction);
        } catch (TransactionNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Get all transactions",
            description = "Retrieves a list of all transactions with pagination",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transactions retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Transaction> transactions = transactionService.getAllTransactions(page, size);
        return ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "Get transactions by account ID",
            description = "Retrieves a list of transactions for a specific account with pagination",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transactions retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))
                    )
            }
    )
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId, page, size);
        return ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "Update a transaction",
            description = "Updates an existing transaction",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transaction updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transaction not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id, @Valid @RequestBody Transaction transaction) {
        transaction.setId(id);
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(transaction);
            return ResponseEntity.ok(updatedTransaction);
        } catch (TransactionNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Delete a transaction",
            description = "Deletes a transaction by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Transaction deleted successfully",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transaction not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        try {
            boolean deleted = transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Get transaction count",
            description = "Returns the total number of transactions",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transaction count retrieved successfully",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping("/count")
    public ResponseEntity<Long> getTransactionCount() {
        long count = transactionService.getTransactionCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/page")
    public String index() {
        return "transactions";
    }
}