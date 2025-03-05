package com.example.openbanking.repository;

import com.example.openbanking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Account} entities.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
public interface AccountRepository extends JpaRepository<Account, String> {}
