package com.example.openbanking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.openbanking.model.Account;
import com.example.openbanking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private final String VALID_IBAN = "DE89370400";
    private final String INVALID_IBAN = "DE000000";



    @BeforeEach
    void setUp() {
        Account account = new Account();
        account.setIban(VALID_IBAN);
        account.setBalance(BigDecimal.valueOf(550));

        lenient().when(accountRepository.findById(VALID_IBAN)).thenReturn(Optional.of(account));
        lenient().when(accountRepository.findById(INVALID_IBAN)).thenReturn(Optional.empty());
    }

    @Test
    void testGetAccountBalance_ValidIban() {
        BigDecimal balance = accountService.getAccountBalance(VALID_IBAN);
        assertEquals(BigDecimal.valueOf(550), balance);
    }

    @Test
    void testGetAccountBalance_InvalidIban() {
        BigDecimal balance = accountService.getAccountBalance(INVALID_IBAN);
        assertEquals(BigDecimal.ZERO, balance);
    }
}
