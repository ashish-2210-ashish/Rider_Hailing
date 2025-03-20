package com.example.Rider_Co.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "Riders")
@Entity
public class Rider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int riderId;
    @Column(nullable = false)
    double coordinateX;
    @Column(nullable = false)
    double coordinateY;
    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ride> rides;
}
