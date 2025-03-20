package com.example.Rider_Co.serviceInterfaces;

import com.example.Rider_Co.models.Rider;

import java.util.List;

public interface RiderServiceInterface {
    List<Rider> getAllRiders();
    Rider getRiderByID(int riderId);
    String addRider(Rider rider);
    String updateRider(Rider rider);
    String deleteRider(int riderId);
    String matchDrivers(int riderId, double destinationCoordinateX, double destinationCoordinateY);

}
