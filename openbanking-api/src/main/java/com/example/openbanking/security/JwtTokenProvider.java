package com.example.openbanking.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Component responsible for generating, parsing, and validating JWT tokens.
 * Uses HMAC SHA256 for signing.
 */
@Component
public class JwtTokenProvider {

    /**
     * Secret key used to sign and verify JWT tokens.
     */
    private final Key secretKey;

    /**
     * Token expiration time in milliseconds.
     */
    private final long expirationMs;

    /**
     * Constructor initializes the JWT provider with a secret key and expiration time.
     *
     * @param secretKey    The secret key used for signing the JWT.
     * @param expirationMs The expiration time of the token in milliseconds.
     */
    public JwtTokenProvider(@Value("${security.jwt.secret-key}") String secretKey,
                            @Value("${security.jwt.expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Generates a JWT token for the given username.
     *
     * @param username The username to include in the token.
     * @return The generated JWT token as a string.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token The JWT token.
     * @return The username (subject) contained in the token.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token The JWT token to validate.
     * @return `true` if the token is valid, otherwise `false`.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}