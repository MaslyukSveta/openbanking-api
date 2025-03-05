package com.example.openbanking.repository;

import com.example.openbanking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


/**
 * Repository interface for managing {@link Transaction} entities.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Retrieves the latest 10 transactions for a given account, ordered by timestamp in descending order.
     *
     * @param accountIban The IBAN of the account for which transactions are retrieved.
     * @return A list of the latest 10 transactions associated with the given account IBAN.
     */
    List<Transaction> findTop10ByAccountIbanOrderByTimestampDesc(String accountIban);
}
