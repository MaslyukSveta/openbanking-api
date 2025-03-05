package com.example.openbanking.controller;

import com.example.openbanking.security.JwtTokenProvider;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for handling authentication and token generation.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor for AuthController.
     * @param jwtTokenProvider Utility class for generating JWT tokens.
     */
    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Generates a JWT token for a sample user.
     * @return A map containing the generated token.
     */
    @GetMapping("/token")
    public Map<String, String> getToken() {
        String token = jwtTokenProvider.generateToken("user@example.com");
        return Map.of("token", token);
    }
}