package com.example.openbanking.service;

import com.example.openbanking.model.Transaction;
import com.example.openbanking.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for handling transaction-related operations.
 * This service interacts with the TransactionRepository to fetch recent transactions.
 */
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    /**
     * Constructor for injecting TransactionRepository dependency.
     *
     * @param transactionRepository Repository to handle transaction persistence.
     */
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves the last 10 transactions for a given account.
     *
     * @param accountIban The IBAN of the account whose transactions are requested.
     * @return A list of the 10 most recent transactions for the account.
     */
    public List<Transaction> getLastTransactions(String accountIban) {
        return transactionRepository.findTop10ByAccountIbanOrderByTimestampDesc(accountIban);
    }
}