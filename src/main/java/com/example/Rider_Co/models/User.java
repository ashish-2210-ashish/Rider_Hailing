package com.example.Rider_Co.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;  //either 'DRIVER' OR 'RIDER'

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private Rider rider;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private Driver driver;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<ApiKeyManager> apiKeyManager;
}
