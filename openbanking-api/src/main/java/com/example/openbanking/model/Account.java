package com.example.openbanking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a bank account entity.
 * This class is used with JPA to interact with the database.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    /**
     * Unique identifier for the bank account (IBAN).
     * Used as the primary key in the database.
     */
    @Id
    private String iban;
    /**
     * The account balance.
     * Stores the current amount of money available in the account.
     */
    private BigDecimal balance;
}
