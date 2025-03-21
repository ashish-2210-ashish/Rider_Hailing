package com.example.Rider_Co.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name ="ApiKeyManager")
public class ApiKeyManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int apiId;

    @OneToOne
    @JoinColumn
    User user;

    @Column(nullable = false)
    private String apiKeyIdentifier;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private  String password;

    @Column(nullable = false)
    private ApiScopes apiScopes;

    @Column(nullable = false)
    private Timestamp createdTime;

    @Column(nullable = false)
    private Timestamp expiringTime;


}
