package com.example.openbanking.service;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service for interacting with an external banking API.
 * This service fetches account balances and transactions from a mock banking API.
 */
@Service
public class ExternalBankingService {
    private final WebClient webClient;

    /**
     * Constructor for injecting the WebClient dependency.
     *
     * @param webClient WebClient used to communicate with the external banking API.
     */
    public ExternalBankingService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Retrieves the balance of an account from the external banking API.
     *
     * @param iban The IBAN of the account whose balance needs to be fetched.
     * @return A {@link Mono} containing the balance as a {@link BigDecimal}.
     */
    public Mono<BigDecimal> getBalance(String iban) {
        return webClient.get()
                .uri("/accounts/{iban}/balance", iban)
                .retrieve()
                .bodyToMono(BigDecimal.class);
    }

    /**
     * Fetches the last transactions of an account from the external banking API.
     *
     * @param iban The IBAN of the account whose transactions need to be fetched.
     * @return A {@link Mono} containing a list of transaction descriptions.
     */
    public Mono<List<String>> getTransactions(String iban) {
        return webClient.get()
                .uri("/accounts/{iban}/transactions", iban)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {});
    }
}