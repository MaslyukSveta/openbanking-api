package com.example.openbanking.service;
import com.example.openbanking.model.Account;
import com.example.openbanking.model.Payment;
import com.example.openbanking.model.Transaction;
import com.example.openbanking.repository.AccountRepository;
import com.example.openbanking.repository.PaymentRepository;
import com.example.openbanking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Service for handling payments between accounts.
 * Ensures transaction safety, account balance updates, and records transactions.
 */
@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor for injecting repository dependencies.
     *
     * @param paymentRepository     Repository for storing payments.
     * @param accountRepository     Repository for managing account balances.
     * @param transactionRepository Repository for recording transactions.
     */
    public PaymentService(PaymentRepository paymentRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Processes a payment between two accounts.
     * The transaction is handled within a @Transactional block to ensure data consistency.
     *
     * @param payment The payment request containing sender and receiver details.
     * @return A message indicating the payment status.
     */
    @Transactional
    public String initiatePayment(Payment payment) {
        log.info("Initiating payment from {} to {} for amount {} {}",
                payment.getSenderIban(), payment.getReceiverIban(), payment.getAmount(), payment.getCurrency());

        validatePaymentRequest(payment);

        Account sender = getAccountByIban(payment.getSenderIban(), "Sender account not found");
        Account receiver = getAccountByIban(payment.getReceiverIban(), "Receiver account not found");

        if (sender.getBalance().compareTo(payment.getAmount()) < 0) {
            log.warn("Insufficient balance for sender: {}", sender.getIban());
            throw new IllegalStateException("Insufficient balance");
        }

        processTransaction(sender, receiver, payment.getAmount());

        payment.setStatus("COMPLETED");
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        saveTransaction(payment.getSenderIban(), payment.getAmount().negate());
        saveTransaction(payment.getReceiverIban(), payment.getAmount());

        log.info("Payment successfully completed between {} and {}", payment.getSenderIban(), payment.getReceiverIban());

        return "Payment completed successfully";
    }

    /**
     * Validates the payment request.
     *
     * @param payment The payment request.
     */
    private void validatePaymentRequest(Payment payment) {
        if (payment.getSenderIban() == null || payment.getReceiverIban() == null) {
            throw new IllegalArgumentException("Sender and Receiver IBANs must be provided.");
        }
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid payment amount.");
        }
        if (payment.getCurrency() == null || payment.getCurrency().isEmpty()) {
            throw new IllegalArgumentException("Currency must be provided.");
        }
    }

    /**
     * Fetches an account by IBAN or throws an exception if not found.
     *
     * @param iban         The account IBAN.
     * @param errorMessage The error message if the account is not found.
     * @return The Account object.
     */
    private Account getAccountByIban(String iban, String errorMessage) {
        return accountRepository.findById(iban)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }

    /**
     * Updates account balances for sender and receiver.
     *
     * @param sender  The sender account.
     * @param receiver The receiver account.
     * @param amount  The transfer amount.
     */
    private void processTransaction(Account sender, Account receiver, BigDecimal amount) {
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.saveAll(List.of(sender, receiver));
    }

    /**
     * Saves a transaction record in the database.
     *
     * @param iban   The account IBAN.
     * @param amount The transaction amount.
     */
    private void saveTransaction(String iban, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountIban(iban);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        log.info("Transaction recorded for account {}: amount {}", iban, amount);
    }
}