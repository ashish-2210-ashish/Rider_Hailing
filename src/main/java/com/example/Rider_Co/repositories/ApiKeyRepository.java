package com.example.Rider_Co.repositories;

import com.example.Rider_Co.models.ApiKeyManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyManager,Integer> {

    List<ApiKeyManager> findByUserId(int userid);

    void deleteByApiKeyIdentifier(String apiKeyIdentifier);

    Optional<ApiKeyManager> findByApiKeyIdentifier(String apiKeyIdentifier);

//    Optional<ApiKeyManager> findByApiKey(String apiKey);

}
