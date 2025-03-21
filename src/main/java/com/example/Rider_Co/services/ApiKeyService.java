package com.example.Rider_Co.services;

import com.example.Rider_Co.models.ApiKeyManager;
import com.example.Rider_Co.models.User;
import com.example.Rider_Co.repositories.ApiKeyRepository;
import com.example.Rider_Co.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {


    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    UserRepository userRepository;

    private  final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public String createApiKey() {

        ApiKeyManager currentApiKeyManager=new ApiKeyManager();
        String username= (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()){
            return "user not found .";
        }

        String rawApiKey= UUID.randomUUID().toString().replace("-","");
        String hashedApiKey= passwordEncoder.encode(rawApiKey);

        String apiKeyIdentifier="..............."+rawApiKey.substring(rawApiKey.length()-6);

        currentApiKeyManager.setUser(userOptional.get());
        currentApiKeyManager.setCreatedTime(LocalDateTime.now());
        currentApiKeyManager.setExpiringTime(LocalDateTime.now().plusMonths(6));
        currentApiKeyManager.setHashesdKey(hashedApiKey);
        currentApiKeyManager.setApiKeyIdentifier(apiKeyIdentifier);

        apiKeyRepository.save(currentApiKeyManager);

        return "\nYour API Key is   "+rawApiKey+"   and it will be displayed once , so better note it down ..\n\n";

    }
}
