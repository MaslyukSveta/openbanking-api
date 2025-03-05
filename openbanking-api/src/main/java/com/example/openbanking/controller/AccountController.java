package com.example.openbanking.controller;

import com.example.openbanking.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for handling account-related operations.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    /**
     * Constructor for AccountController.
     * @param accountService Service for handling account-related operations.
     */
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Retrieves the balance of the specified account.
     * @param iban The IBAN of the account.
     * @return The current balance of the account.
     */
    @GetMapping("/{iban}/balance")
    public BigDecimal getAccountBalance(@PathVariable String iban) {
        return accountService.getAccountBalance(iban);
    }
}