package com.example.Rider_Co.repositories;

import com.example.Rider_Co.models.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderRepository extends JpaRepository<Rider,Integer> {
}
