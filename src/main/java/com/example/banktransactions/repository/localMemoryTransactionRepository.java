package com.example.banktransactions.repository;

import com.example.banktransactions.model.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class localMemoryTransactionRepository {

    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();
    private final Set<String> usedIds = Collections.synchronizedSet(new HashSet<>());

    public Transaction create(Transaction transaction) {
        if(StringUtils.isEmpty(transaction.getId())){
            transaction.setId(generateUniqueId());
        }
        String id = transaction.getId();
        transactions.put(id, transaction);
        return transaction;
    }

    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactions.get(id));
    }

    public List<Transaction> findAll(int page, int size) {
        List<Transaction> allTransactions = new ArrayList<>(transactions.values());
        int total = allTransactions.size();
        int start = page * size;
        if (start > total) {
            return Collections.emptyList();
        }
        int end = Math.min(start + size, total);
        return allTransactions.subList(start, end);
    }

    public List<Transaction> findByAccountId(String accountId, int page, int size) {
        List<Transaction> accountTransactions = transactions.values().stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .collect(Collectors.toList());

        int total = accountTransactions.size();
        int start = page * size;
        if (start > total) {
            return Collections.emptyList();
        }
        int end = Math.min(start + size, total);
        return accountTransactions.subList(start, end);
    }

    public boolean update(Transaction transaction) {
        if (transactions.containsKey(transaction.getId())) {
            transactions.put(transaction.getId(), transaction);
            return true;
        }
        return false;
    }

    public boolean delete(String id) {
        return transactions.remove(id) != null;
    }

    public long count() {
        return transactions.size();
    }

    private String generateUniqueId() {
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }
}