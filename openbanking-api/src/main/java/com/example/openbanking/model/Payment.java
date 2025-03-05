package com.example.openbanking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a payment transaction between two bank accounts.
 * This entity is used to store payment details in the database.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    /**
     * Unique identifier for the payment.
     * Auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * IBAN of the sender's account.
     */
    private String senderIban;

    /**
     * IBAN of the receiver's account.
     */
    private String receiverIban;

    /**
     * The amount of money being transferred.
     */
    private BigDecimal amount;

    /**
     * The currency of the transaction (e.g., EUR, USD).
     */
    private String currency;

    /**
     * The status of the payment (e.g., PENDING, COMPLETED, FAILED).
     */
    private String status;

    /**
     * The timestamp when the payment was created.
     */
    private LocalDateTime createdAt;
}