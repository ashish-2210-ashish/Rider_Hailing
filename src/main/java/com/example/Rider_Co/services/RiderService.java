package com.example.Rider_Co.services;

import com.example.Rider_Co.models.Driver;
import com.example.Rider_Co.models.Ride;
import com.example.Rider_Co.models.Rider;
import com.example.Rider_Co.repositories.DriverRepository;
import com.example.Rider_Co.repositories.RideRepository;
import com.example.Rider_Co.repositories.RiderRepository;
import com.example.Rider_Co.serviceInterfaces.RiderServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RiderService implements RiderServiceInterface {

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    private static final Logger logger = LoggerFactory.getLogger(RiderService.class);

    /**
     * Fetches all riders from the database.
     * @return List of riders.
     */
    @Override
    public List<Rider> getAllRiders() {
        logger.info("Fetching all riders from the database");
        return riderRepository.findAll();
    }

    /**
     * Retrieves a rider by their ID.
     * @param riderId The ID of the rider.
     * @return Rider object or an empty Rider if not found.
     */
    @Override
    public Rider getRiderByID(int riderId) {
        return riderRepository.findById(riderId)
                .orElseGet(() -> {
                    logger.warn("Rider with ID: {} not found", riderId);
                    return new Rider();
                });
    }

    /**
     * Adds a new rider to the system.
     * @param rider The Rider object to add.
     * @return Status message.
     */
    @Override
    public String addRider(Rider rider) {
        riderRepository.save(rider);
        logger.info("Rider with ID: {} added successfully", rider.getRiderId());
        return "Successfully added the rider with ID: " + rider.getRiderId();
    }

    /**
     * Updates an existing rider's details.
     * @param rider The Rider object with updated information.
     * @return Status message.
     */
    @Override
    public String updateRider(Rider rider) {
        if (!riderRepository.existsById(rider.getRiderId())) {
            logger.warn("Rider with ID: {} not found. Update failed.", rider.getRiderId());
            return "Rider with ID: " + rider.getRiderId() + " does not exist.";
        }

        riderRepository.save(rider);
        logger.info("Rider with ID: {} updated successfully", rider.getRiderId());
        return "Successfully updated the rider with ID: " + rider.getRiderId();
    }

    /**
     * Deletes a rider by their ID.
     * @param riderId The ID of the rider to delete.
     * @return Status message.
     */
    @Override
    public String deleteRider(int riderId) {
        if (!riderRepository.existsById(riderId)) {
            logger.warn("Rider with ID: {} not found. Deletion failed.", riderId);
            return "Rider with ID: " + riderId + " does not exist.";
        }

        riderRepository.deleteById(riderId);
        logger.info("Rider with ID: {} deleted successfully", riderId);
        return "Successfully deleted the rider with ID: " + riderId;
    }

    /**
     * Matches a rider with available drivers based on their start and destination coordinates.
     * @param riderId The ID of the rider.
     * @param destinationCoordinateX Destination X-coordinate.
     * @param destinationCoordinateY Destination Y-coordinate.
     * @return Status message.
     */
    @Override
    public String matchDrivers(int riderId, double destinationCoordinateX, double destinationCoordinateY) {
        Rider rider = riderRepository.findById(riderId).orElse(null);
        if (rider == null) {
            logger.warn("Matching failed: Rider with ID {} not found.", riderId);
            return "RIDER_NOT_FOUND";
        }

        // Debugging: Log received values
        logger.info("Received destination coordinates: X = {}, Y = {}", destinationCoordinateX, destinationCoordinateY);

        // Check if the rider already has an ongoing ride
        List<Ride> existingRides = rideRepository.findByRider_RiderIdAndIsCompleted(riderId, false);
        if (!existingRides.isEmpty()) {
            logger.warn("Rider with ID: {} already has an active ride.", riderId);
            return "RIDE_ALREADY_EXISTS_FOR_RIDER " + riderId;
        }

        // Create a new ride request
        Ride ride = new Ride();
        ride.setDriver(null);
        ride.setRider(rider);
        ride.setPickupCoordinateX(rider.getCoordinateX());
        ride.setPickupCoordinateY(rider.getCoordinateY());
        ride.setDestinationCoordinateX(destinationCoordinateX);
        ride.setDestinationCoordinateY(destinationCoordinateY);
        ride.setRideFare(0);
        ride.setTimeTaken(0);

        // Debugging: Log ride details before saving
        logger.info("Saving ride with destination: X = {}, Y = {}", ride.getDestinationCoordinateX(), ride.getDestinationCoordinateY());

        rideRepository.save(ride);

        return "Successfully added the ride for rider " + riderId;
    }

}
