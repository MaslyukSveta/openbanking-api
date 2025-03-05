package com.example.openbanking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient.
 * Defines a WebClient bean for making external API requests.
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a WebClient instance with a base URL.
     * Used to communicate with external services.
     * @return Configured WebClient instance.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:8080/mock-bank").build();
    }
}

