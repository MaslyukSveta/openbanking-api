package com.example.openbanking.config;

import com.example.openbanking.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Security configuration for handling authentication and authorization.
 * Includes JWT authentication, password encoding, and security filters.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);


    @Value("${security.jwt.secret-key}")
    private String secretKey;


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for SecurityConfig.
     * @param jwtAuthenticationFilter JWT authentication filter.
     * @param userDetailsService Service to retrieve user details.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        logger.info("SecurityConfig initialized.");
    }

    /**
     * Configures security filter chain.
     * Disables CSRF, enforces stateless session policy, and sets authorization rules.
     * @param http HttpSecurity object for security configuration.
     * @return Configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain...");
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        logger.info("Security filter chain configured successfully.");

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Provides authentication manager for processing authentication requests.
     * @param authenticationConfiguration Authentication configuration.
     * @return Configured AuthenticationManager.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        logger.info("Configuring AuthenticationManager...");
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures JWT decoder to validate tokens using a secret key.
     * @return Configured JwtDecoder.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        logger.info("Initializing JWT decoder...");
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256")).build();
    }

    /**
     * Configures JWT encoder to generate JWT tokens.
     * @return Configured JwtEncoder.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        logger.info("Initializing JWT encoder...");
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Provides password encoder using BCrypt hashing.
     * @return Configured PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Initializing password encoder...");
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures authentication provider for user authentication.
     * Uses DaoAuthenticationProvider with UserDetailsService and password encoder.
     * @return Configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        logger.info("Configuring AuthenticationProvider...");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}