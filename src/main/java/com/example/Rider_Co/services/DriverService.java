package com.example.Rider_Co.services;

import com.example.Rider_Co.models.Driver;
import com.example.Rider_Co.models.Ride;
import com.example.Rider_Co.models.RideStatus;
import com.example.Rider_Co.repositories.DriverRepository;
import com.example.Rider_Co.repositories.RideRepository;
import com.example.Rider_Co.serviceInterfaces.DriverServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverService implements DriverServiceInterface {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RideRepository rideRepository;

    private static final Logger logger = LoggerFactory.getLogger(DriverService.class);

    /**
     * Retrieves all drivers from the database.
     * @return List of all drivers.
     */
    @Override
    public List<Driver> GetAllDrivers() {
        logger.info("Fetching all drivers from the database");
        return driverRepository.findAll();
    }

    /**
     * Retrieves a driver by their ID.
     * @param driverId ID of the driver.
     * @return Driver object or an empty Driver if not found.
     */
    @Override
    public Driver GetDriverByID(int driverId) {
        if (driverRepository.existsById(driverId)) {
            logger.info("Fetching driver with ID: {}", driverId);
            return driverRepository.findById(driverId).orElse(new Driver());
        } else {
            logger.warn("Driver with ID: {} does not exist", driverId);
            return new Driver();
        }
    }

    /**
     * Adds a new driver to the system.
     * @param driver Driver object to be added.
     * @return Success message.
     */
    @Override
    public String AddDriver(Driver driver) {
        driverRepository.save(driver);
        logger.info("Driver with ID: {} added successfully", driver.getDriverId());
        return "Successfully added the driver with ID: " + driver.getDriverId();
    }

    /**
     * Updates an existing driver's details.
     * @param driver Driver object with updated details.
     * @return Success or failure message.
     */
    @Override
    public String UpdateDriver(Driver driver) {
        if (driverRepository.existsById(driver.getDriverId())) {
            driverRepository.save(driver);
            logger.info("Driver with ID: {} updated successfully", driver.getDriverId());
            return "Successfully updated the driver with ID: " + driver.getDriverId();
        } else {
            logger.warn("Driver with ID: {} does not exist and cannot be updated", driver.getDriverId());
            return "Driver with ID: " + driver.getDriverId() + " does not exist.";
        }
    }

    /**
     * Deletes a driver by their ID.
     * @param driverId ID of the driver to be deleted.
     * @return Success or failure message.
     */
    @Override
    public String DeleteDriver(int driverId) {
        if (driverRepository.existsById(driverId)) {
            driverRepository.deleteById(driverId);
            logger.info("Driver with ID: {} deleted successfully", driverId);
            return "Successfully deleted the driver with ID: " + driverId;
        } else {
            logger.warn("Driver with ID: {} does not exist and cannot be deleted", driverId);
            return "Driver with ID: " + driverId + " does not exist.";
        }
    }

    /**
     * Assigns a ride to a driver.
     * @param driverId ID of the driver.
     * @param rideId ID of the ride.
     * @return Status message of ride acceptance.
     */
    @Override
    public String AcceptRide(int driverId, int rideId) {
        Ride selectedRide = rideRepository.findById(rideId).orElse(null);
        Driver driver=driverRepository.findById(driverId).orElse(null);

        if (selectedRide == null) {
            logger.warn("Ride with ID: {} is already assigned or does not exist", rideId);
            return "Ride is already assigned to someone else.";
        }

        if (driver == null) {
            logger.warn("Driver with ID: {} doesn't exists", driverId);
            return "Driver doesn't exists .";
        }

        selectedRide.setDriver(driver);
        selectedRide.setStatus(RideStatus.AWAITING_PICKUP);
        selectedRide.setRideAccepted(true);
        rideRepository.save(selectedRide);
        logger.info("Ride with ID: {} assigned to driver ID: {}", rideId, driverId);

        // Mark driver as unavailable after accepting a ride
        if (driver != null) {
            driver.setAvailable(false);
            driverRepository.save(driver);
            logger.info("Driver with ID: {} is now unavailable", driverId);
        }

        return "RIDE_ACCEPTED " + rideId;
    }

    /**
     * Retrieves a list of available rides for a specific driver, sorted by distance.
     * @param driverId ID of the driver.
     * @return List of available rides sorted by distance.
     */
    @Override
    public List<Ride> getAvailableRidesForDriver(int driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);

        if (driver == null) {
            logger.warn("Driver with ID: {} does not exist", driverId);
            return new ArrayList<>();
        }

        if (!driver.isAvailable()) {
            logger.info("Driver with ID: {} is not available for new rides", driverId);
            return new ArrayList<>();
        }

        double driverX = driver.getCoordinateX();
        double driverY = driver.getCoordinateY();

        List<Ride> availableRides = rideRepository.findAllUnassignedRide(RideStatus.AVAILABLE_RIDE);


        // Sort rides based on the distance from the driver
        availableRides.sort((r1, r2) -> {
            double d1 = calculateDistance(driverX, driverY, r1.getPickupCoordinateX(), r1.getPickupCoordinateY());
            double d2 = calculateDistance(driverX, driverY, r2.getDestinationCoordinateX(), r2.getDestinationCoordinateY());
            return Double.compare(d1, d2);
        });

        logger.info("Returning sorted available rides for driver ID: {}", driverId);
        return availableRides;
    }

    /**
     * Calculates the Euclidean distance between two points.
     * @param x1 X-coordinate of the first point.
     * @param y1 Y-coordinate of the first point.
     * @param x2 X-coordinate of the second point.
     * @param y2 Y-coordinate of the second point.
     * @return Distance between the two points.
     */
    @Override
    public double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
