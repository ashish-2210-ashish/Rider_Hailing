package com.example.Rider_Co.jwtUtils;

import com.example.Rider_Co.models.ApiKeyManager;
import com.example.Rider_Co.repositories.ApiKeyRepository;
import com.example.Rider_Co.services.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;



public class ApiKeyAuthFilter extends OncePerRequestFilter {



    private static  final Logger logger = LoggerFactory.getLogger(ApiKeyService.class);


    private final ApiKeyRepository apiKeyRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApiKeyService apiKeyService;



    public ApiKeyAuthFilter(ApiKeyService apiKeyService,
                            ApiKeyRepository apiKeyRepository,
                            BCryptPasswordEncoder passwordEncoder) {
        this.apiKeyService = apiKeyService;
        this.apiKeyRepository = apiKeyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String apiKeyHeader = request.getHeader("X-API-KEY");

        if (apiKeyHeader != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            for (ApiKeyManager apiKeyManager : apiKeyRepository.findAll()) {
                if (passwordEncoder.matches(apiKeyHeader, apiKeyManager.getHashesdKey())) {

                    String username = apiKeyManager.getUser().getUsername();

                    logger.info("\n user with user id : {}  is trying to use the api end points  ..\n ",username);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    break;
                }
            }
        }
        chain.doFilter(request, response);
    }

}
