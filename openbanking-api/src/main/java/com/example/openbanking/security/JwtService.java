package com.example.openbanking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Service class responsible for JWT (JSON Web Token) operations.
 * This class handles token generation, validation, and extraction of claims.
 */
@Service
public class JwtService {

    /**
     * Secret key used for signing the JWT token.
     * Loaded from application properties.
     */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * JWT expiration time in milliseconds.
     * Loaded from application properties.
     */
    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Generates the signing key for JWT using HMAC SHA algorithm.
     *
     * @return The generated key used for signing tokens.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token The JWT token.
     * @return The username contained in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token The JWT token.
     * @param claimsResolver Function to resolve the specific claim.
     * @param <T> The type of the claim to be extracted.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a new JWT token for the given username.
     *
     * @param username The username for which the token is generated.
     * @return A signed JWT token.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}