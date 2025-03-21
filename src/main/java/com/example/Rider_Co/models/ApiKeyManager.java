package com.example.Rider_Co.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name ="ApiKeyManager")
public class ApiKeyManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int apiId;

    @ManyToOne
    @JoinColumn
    User user;

    @Column(nullable = false)
    private String apiKeyIdentifier;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private  String hashesdKey;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime expiringTime;


}
