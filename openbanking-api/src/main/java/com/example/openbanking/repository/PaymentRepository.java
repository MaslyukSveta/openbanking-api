package com.example.openbanking.repository;

import com.example.openbanking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Payment} entities.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {}
