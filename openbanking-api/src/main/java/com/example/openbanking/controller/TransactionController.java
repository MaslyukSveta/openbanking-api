package com.example.openbanking.controller;

import com.example.openbanking.model.Transaction;
import com.example.openbanking.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for handling transaction-related operations.
 */
@RestController
@RequestMapping("/api/accounts")
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * Constructor for TransactionController.
     * @param transactionService Service for handling transaction operations.
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Retrieves the last 10 transactions for the given account.
     * @param accountId The IBAN of the account.
     * @return A list of recent transactions for the specified account.
     */
    @GetMapping("/{accountId}/transactions")
    public List<Transaction> getTransactions(@PathVariable String accountId) {
        return transactionService.getLastTransactions(accountId);
    }
}
