package com.example.openbanking.service;

import com.example.openbanking.model.Account;
import com.example.openbanking.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service class for handling account-related operations.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Constructor for injecting the {@link AccountRepository} dependency.
     *
     * @param accountRepository Repository for account data access.
     */
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieves the balance of an account by its IBAN.
     *
     * @param iban The IBAN of the account to retrieve the balance for.
     * @return The balance of the account, or BigDecimal.ZERO if the account is not found.
     */
    public BigDecimal getAccountBalance(String iban) {
        Optional<Account> account = accountRepository.findById(iban);
        return account.map(Account::getBalance).orElse(BigDecimal.ZERO);
    }
}