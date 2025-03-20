package com.example.Rider_Co.serviceInterfaces;

import com.example.Rider_Co.models.Ride;

import java.util.List;

public interface RideServiceInterface {
    List<Ride> getAllRides();
    Ride getRideByID(int rideId);
    String deleteRide(int rideId);
    String stopRide(int rideId, double timeTaken);
    String cancelRide(int rideId);
    String billRide(int rideId);
    double calculateDistance(double x1, double y1, double x2, double y2);
    List<Ride> getRideHistoryByDriver(int driverId);
    List<Ride> getRideHistoryByRider(int riderId);
    String startRide(int driverId,int rideId);

}
