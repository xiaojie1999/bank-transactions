package com.example.banktransactions.service;

import com.example.banktransactions.model.Transaction;
import com.example.banktransactions.repository.localMemoryTransactionRepository;
import com.example.banktransactions.exception.TransactionAlreadyExistsException;
import com.example.banktransactions.exception.TransactionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    private final localMemoryTransactionRepository repository;

    public TransactionService(localMemoryTransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getId() != null && repository.findById(transaction.getId()).isPresent()) {
            throw new TransactionAlreadyExistsException("Transaction with ID " + transaction.getId() + " already exists");
        }
        return repository.create(transaction);
    }

    @Cacheable(value = "transactions", key = "#id")
    public Transaction getTransactionById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with ID " + id + " not found"));
    }

    public List<Transaction> getAllTransactions(int page, int size) {
        return repository.findAll(page, size);
    }

    public List<Transaction> getTransactionsByAccountId(String accountId, int page, int size) {
        return repository.findByAccountId(accountId, page, size);
    }

    public Transaction updateTransaction(Transaction transaction) {
        Optional<Transaction> existingTransaction = repository.findById(transaction.getId());
        if (!existingTransaction.isPresent()) {
            throw new TransactionNotFoundException("Transaction with ID " + transaction.getId() + " not found");
        }
        Transaction orinTransaction =  existingTransaction.get();
        if (transaction.getAccountId() != null && !transaction.getAccountId().equals(orinTransaction.getAccountId())) {
            throw new TransactionNotFoundException("accountId cannot be changed");
        }
        if (transaction.getType() != null && !transaction.getType().equals(orinTransaction.getType())) {
            orinTransaction.setType(transaction.getType());
        }
        if (transaction.getAmount() != null && !transaction.getAmount().equals(orinTransaction.getAmount())) {
            orinTransaction.setAmount(transaction.getAmount());
        }
        if (transaction.getDescription() != null && !transaction.getDescription().equals(orinTransaction.getDescription())) {
            orinTransaction.setDescription(transaction.getDescription());
        }
        return repository.update(transaction) ? transaction : null;
    }

    public boolean deleteTransaction(String id) {
        if (!repository.findById(id).isPresent()) {
            throw new TransactionNotFoundException("Transaction with ID " + id + " not found");
        }
        return repository.delete(id);
    }

    public long getTransactionCount() {
        return repository.count();
    }
}