package com.example.banktransactions;
import com.example.banktransactions.controller.TransactionController;
import com.example.banktransactions.model.Transaction;
import com.example.banktransactions.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(TransactionController.class)

public class TestCase {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setId("1");
        testTransaction.setAccountId("ACC123");
        testTransaction.setAmount(BigDecimal.valueOf(100.5));
        testTransaction.setType("DEPOSIT");
        testTransaction.setDescription("Test transaction");
    }


    @Test
    void testCreateTransaction() throws Exception {
        Mockito.when(transactionService.createTransaction(any(Transaction.class))).thenReturn(testTransaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\":\"ACC123\",\"amount\":100.5,\"type\":\"DEPOSIT\",\"description\":\"Test transaction\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value("ACC123"))
                .andExpect(jsonPath("$.amount").value("100.5"))
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.description").value("Test transaction"));

    }

    @Test
    void testGetTransactionById() throws Exception {
        Mockito.when(transactionService.getTransactionById("1")).thenReturn(testTransaction);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountId").value("ACC123"));

    }
    @Test
    void testUpdateTransaction() throws Exception {
//        Transaction transaction = new Transaction("ACC123",BigDecimal.valueOf(200), "DEPOSIT", "Test transaction");
        Mockito.when(transactionService.updateTransaction(any(Transaction.class))).thenReturn(testTransaction);
        mockMvc.perform(put("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\":\"ACC123\",\"amount\":100,\"type\":\"DEPOSIT\",\"description\":\"Test transaction\"}"  ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("ACC123"))
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.description").value("Test transaction"));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        Mockito.when(transactionService.deleteTransaction("1")).thenReturn(true);

        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isNoContent());
    }

}
