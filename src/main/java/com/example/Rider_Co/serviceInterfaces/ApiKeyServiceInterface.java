package com.example.Rider_Co.serviceInterfaces;

import com.example.Rider_Co.models.ApiKeyManager;

import java.util.List;

public interface ApiKeyServiceInterface {
    String createApiKey();
    List<ApiKeyManager> getApiKeysList();
    String deleteApiKey(String apiKeyIdentifier);


}
