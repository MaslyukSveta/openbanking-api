package com.example.openbanking.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.openbanking.model.Account;
import com.example.openbanking.model.Payment;
import com.example.openbanking.model.Transaction;
import com.example.openbanking.repository.AccountRepository;
import com.example.openbanking.repository.PaymentRepository;
import com.example.openbanking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;



@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Account sender;
    private Account receiver;
    private Payment payment;

    @BeforeEach
    void setUp() {
        sender = new Account("DE123456", BigDecimal.valueOf(1000));
        receiver = new Account("DE654321", BigDecimal.valueOf(500));

        payment = new Payment();
        payment.setSenderIban(sender.getIban());
        payment.setReceiverIban(receiver.getIban());
        payment.setAmount(BigDecimal.valueOf(200));
        payment.setCurrency("EUR");
    }

    @Test
    void testInitiatePayment_Success() {
        when(accountRepository.findById(sender.getIban())).thenReturn(Optional.of(sender));
        when(accountRepository.findById(receiver.getIban())).thenReturn(Optional.of(receiver));

        String result = paymentService.initiatePayment(payment);

        assertEquals("Payment completed successfully", result);
        assertEquals(BigDecimal.valueOf(800), sender.getBalance());
        assertEquals(BigDecimal.valueOf(700), receiver.getBalance());

        verify(accountRepository, times(1)).saveAll(List.of(sender, receiver));
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void testInitiatePayment_InsufficientBalance() {
        payment.setAmount(BigDecimal.valueOf(1500));

        when(accountRepository.findById(sender.getIban())).thenReturn(Optional.of(sender));
        when(accountRepository.findById(receiver.getIban())).thenReturn(Optional.of(receiver));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            paymentService.initiatePayment(payment);
        });

        assertEquals("Insufficient balance", exception.getMessage());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void testInitiatePayment_SameSenderReceiver() {
        payment.setReceiverIban(sender.getIban());

        when(accountRepository.findById(sender.getIban())).thenReturn(Optional.of(sender));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.initiatePayment(payment);
        });

        assertEquals("Sender and receiver accounts cannot be the same", exception.getMessage());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void testInitiatePayment_MissingSenderIban() {
        payment.setSenderIban(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.initiatePayment(payment);
        });

        assertEquals("Sender and Receiver IBANs must be provided.", exception.getMessage());
    }

    @Test
    void testInitiatePayment_MissingAmount() {
        payment.setAmount(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.initiatePayment(payment);
        });

        assertEquals("Invalid payment amount.", exception.getMessage());
    }
}
