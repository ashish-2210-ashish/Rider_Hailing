package com.example.Rider_Co.security;

import com.example.Rider_Co.authFilters.JwtAuthFilter;
import com.example.Rider_Co.authFilters.ApiKeyAuthFilter;
import com.example.Rider_Co.repositories.ApiKeyRepository;
import com.example.Rider_Co.services.ApiKeyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Value("${security.whitelist}")
    private String[] whitelist;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Define ApiKeyAuthFilter Bean properly with dependencies
    @Bean
    public ApiKeyAuthFilter apiKeyAuthFilter(ApiKeyService apiKeyService,
                                             ApiKeyRepository apiKeyRepository,
                                             BCryptPasswordEncoder passwordEncoder) {
        return new ApiKeyAuthFilter(apiKeyService, apiKeyRepository, passwordEncoder);
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http,
//                                                   ApiKeyAuthFilter apiKeyAuthFilter) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                                .requestMatchers(whitelist).permitAll()
//                        .requestMatchers("/driver/**","/rider/**", "/ride/**","/api/**","/user/**").hasRole("ADMIN")
//                                .requestMatchers("/driver/**", "/ride/**").hasRole("DRIVER")
//                                .requestMatchers("/rider/**").hasRole("RIDER")
//                                .requestMatchers("/api/**").hasAnyRole("DRIVER", "RIDER")
//                                .anyRequest().authenticated()
//        )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)  // API Key first
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);    // Then JWT
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ApiKeyAuthFilter apiKeyAuthFilter) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Enable CORS
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 💥 Allow ALL requests unconditionally
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
