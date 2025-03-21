package com.example.Rider_Co.services;

import com.example.Rider_Co.models.ApiKeyManager;
import com.example.Rider_Co.models.User;
import com.example.Rider_Co.repositories.ApiKeyRepository;
import com.example.Rider_Co.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {


    private static final Logger logger = LoggerFactory.getLogger(ApiKeyService.class);

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String createApiKey() {


        ApiKeyManager currentApiKeyManager = new ApiKeyManager();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "user not found .";
        }

        if (userOptional.get().getApiKeyManager().size() >= 3) {
            logger.info("user already has 3 API keys and they are :  {}", userOptional.get().getApiKeyManager());
            return "\n A user can have maximum of 3 keys , so delete any of your existing key to create a new key ..\n";
        } else {

            String rawApiKey = UUID.randomUUID().toString().replace("-", "");
            String hashedApiKey = passwordEncoder.encode(rawApiKey);

            String apiKeyIdentifier = "..............." + rawApiKey.substring(rawApiKey.length() - 6);

            currentApiKeyManager.setUser(userOptional.get());
            currentApiKeyManager.setCreatedTime(LocalDateTime.now());
            currentApiKeyManager.setExpiringTime(LocalDateTime.now().plusMonths(6));
            currentApiKeyManager.setHashesdKey(hashedApiKey);
            currentApiKeyManager.setApiKeyIdentifier(apiKeyIdentifier);

            apiKeyRepository.save(currentApiKeyManager);

            return "\nYour API Key is   " + rawApiKey + "   and it will be displayed once , so better note it down ..\n\n";

        }
    }

    public List<ApiKeyManager> getApiKeysList() {

        ApiKeyManager currentApiKeyManager = new ApiKeyManager();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found"));

        return apiKeyRepository.findByUserId(user.getId());

    }

    public String deleteApiKey(String apiKeyIdentifier) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found .."));

        ApiKeyManager apiKeyManager = apiKeyRepository.findByApiKeyIdentifier("..............." + apiKeyIdentifier).orElseThrow(() -> new RuntimeException("provided API key is invalid"));

        if (user.getId() == apiKeyManager.getUser().getId()) {
            apiKeyRepository.delete(apiKeyManager);
            return "\n specified API key end with "+ apiKeyIdentifier+" is deleted successfully ..\n ";
        } else {
            return "\nyou don't have credentials to delete the specified api key , only owner can delete it ...\n";
        }

    }
}


