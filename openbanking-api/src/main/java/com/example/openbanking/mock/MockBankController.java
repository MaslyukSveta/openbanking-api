package com.example.openbanking.mock;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Mock implementation of a banking service.
 * This controller simulates external bank API responses
 * for testing purposes.
 */
@RestController
@RequestMapping("/mock-bank")
public class MockBankController {

    /**
     * Simulates retrieving the balance of an account.
     * This method always returns a fixed balance for testing.
     *
     * @param iban The International Bank Account Number (IBAN) of the account.
     * @return A fixed balance of 1000.00 for all IBANs.
     */
    @GetMapping("/accounts/{iban}/balance")
    public BigDecimal getMockBalance(@PathVariable String iban) {
        return BigDecimal.valueOf(1000.00);
    }

    /**
     * Simulates retrieving a list of recent transactions for an account.
     * This method returns a predefined list of transactions for testing.
     *
     * @param iban The IBAN of the account.
     * @return A list of mock transactions.
     */
    @GetMapping("/accounts/{iban}/transactions")
    public List<String> getMockTransactions(@PathVariable String iban) {
        return List.of(
                "TXN001: -50.00",
                "TXN002: -20.00",
                "TXN003: +200.00"
        );
    }
}
