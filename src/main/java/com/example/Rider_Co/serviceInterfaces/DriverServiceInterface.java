package com.example.Rider_Co.serviceInterfaces;

import com.example.Rider_Co.models.Driver;
import com.example.Rider_Co.models.Ride;

import java.util.List;

public interface DriverServiceInterface {

    List<Driver> GetAllDrivers();
    Driver GetDriverByID(int driverId);
    String AddDriver(Driver driver);
    String UpdateDriver(Driver driver);
    String DeleteDriver(int driverId);
    String AcceptRide(int driverId, int rideId);
    List<Ride> getAvailableRidesForDriver(int driverId);
    double calculateDistance(double x1, double y1, double x2, double y2);
}
