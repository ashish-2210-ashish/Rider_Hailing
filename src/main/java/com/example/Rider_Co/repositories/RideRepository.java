package com.example.Rider_Co.repositories;

import com.example.Rider_Co.models.Ride;
import com.example.Rider_Co.models.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Integer> {

    @Query("SELECT r FROM Ride r WHERE r.rider.riderId = :riderId AND r.isCompleted = :isCompleted")
    List<Ride> findByRider_RiderIdAndIsCompleted(@Param("riderId") int riderId, @Param("isCompleted") boolean isCompleted);

    @Query("SELECT r FROM Ride r WHERE r.status = :status")
    List<Ride> findAllUnassignedRide(@Param("status") RideStatus status);

    // Fetch rides by Rider ID
    List<Ride> findByRider_RiderId(int riderId);

    // Fetch rides by Driver ID
    List<Ride> findByDriver_DriverId(int driverId);
}
