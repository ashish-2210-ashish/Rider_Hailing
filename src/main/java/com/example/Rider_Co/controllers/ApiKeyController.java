package com.example.Rider_Co.controllers;

import com.example.Rider_Co.models.ApiKeyManager;
import com.example.Rider_Co.services.ApiKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiKeyController {

    @Autowired
    ApiKeyService apiKeyService;

    private static final Logger logger= LoggerFactory.getLogger(ApiKeyController.class);

    @PostMapping
    public ResponseEntity<String> createApi(@RequestBody ApiKeyManager apiKeyManager){
        try {
            String apiKey = apiKeyService.createApiKey();
            logger.info("API key created successfully -> API key : {}", apiKey);
            return  ResponseEntity.status(HttpStatus.CREATED).body(apiKey);
        }
        catch (Exception e){
            logger.error("Error Creating API key : ",e);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Filed to create API key");
        }

    }
}
