package com.example.openbanking.security;

import java.time.Instant;
import java.time.Duration;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

/**
 * Utility class for generating JWT tokens.
 * Uses Spring Security's {@link JwtEncoder} to create tokens with specific claims.
 */
@Component
public class JwtUtil {

    /**
     * JWT encoder for signing and generating tokens.
     */
    private final JwtEncoder jwtEncoder;

    /**
     * Constructor to initialize the JWT encoder.
     *
     * @param jwtEncoder The {@link JwtEncoder} instance used for encoding JWTs.
     */
    public JwtUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a JWT token for a given username.
     *
     * @param username The username to include in the token.
     * @return A signed JWT token as a {@link String}.
     */
    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(Duration.ofHours(1));

        // Create JWT claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("openbanking-api")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(username)
                .claim("roles", "USER")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}