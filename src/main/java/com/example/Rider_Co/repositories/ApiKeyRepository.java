package com.example.Rider_Co.repositories;

import com.example.Rider_Co.models.ApiKeyManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyManager,Integer> {
}
