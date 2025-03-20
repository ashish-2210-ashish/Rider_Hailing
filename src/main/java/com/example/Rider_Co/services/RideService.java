package com.example.Rider_Co.services;

import com.example.Rider_Co.models.Driver;
import com.example.Rider_Co.models.Ride;
import com.example.Rider_Co.models.RideStatus;
import com.example.Rider_Co.repositories.DriverRepository;
import com.example.Rider_Co.repositories.RideRepository;
import com.example.Rider_Co.serviceInterfaces.DriverServiceInterface;
import com.example.Rider_Co.serviceInterfaces.RideServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService implements RideServiceInterface {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverServiceInterface driverService;

    private static final Logger logger = LoggerFactory.getLogger(RideService.class);

    @Override
    public List<Ride> getAllRides() {
        logger.info("Fetching all rides from the database");
        return rideRepository.findAll();
    }

    @Override
    public Ride getRideByID(int rideId) {
        return rideRepository.findById(rideId)
                .orElseGet(() -> {
                    logger.warn("Ride with ID {} not found.", rideId);
                    return new Ride();
                });
    }



    @Override
    public String deleteRide(int rideId) {
        if (!rideRepository.existsById(rideId)) {
            logger.warn("Ride with ID: {} does not exist, deletion failed.", rideId);
            return "Ride with ID: " + rideId + " does not exist.";
        }

        rideRepository.deleteById(rideId);
        logger.info("Ride with ID: {} deleted successfully", rideId);
        return "Successfully deleted ride with ID: " + rideId;
    }

    @Override
    public String stopRide(int rideId, double timeTaken) {
        Ride currentRide = rideRepository.findById(rideId).orElse(null);

        if (currentRide == null) {
            logger.warn("Ride {} not found, cannot stop.", rideId);
            return "Ride " + rideId + " does not exist.";
        }

        if (currentRide.isCompleted()) {
            logger.warn("Ride {} is already completed.", rideId);
            return "Ride " + rideId + " is already completed.";
        }

        double currentCoordinateX=currentRide.getDestinationCoordinateX();
        double currentCoordinateY=currentRide.getDestinationCoordinateY();


        currentRide.setCompleted(true);
        currentRide.setTimeTaken(timeTaken);
        currentRide.setStatus(RideStatus.COMPLETED);
        rideRepository.save(currentRide);

        Driver currentDriver = driverRepository.findById(currentRide.getDriver().getDriverId()).orElse(null);
        assert currentDriver != null;
        currentDriver.setCoordinateX(currentCoordinateX);
        currentDriver.setCoordinateY(currentCoordinateY);
        driverRepository.save(currentDriver);

        // Free up the driver
        releaseDriver(currentRide.getDriver().getDriverId());

        logger.info("Ride {} stopped successfully.", rideId);
        return "Ride " + rideId + " successfully stopped.";
    }

    @Override
    public String cancelRide(int rideId) {
        Ride currentRide = rideRepository.findById(rideId).orElse(null);

        if (currentRide == null) {
            logger.warn("Ride {} not found, cannot cancel.", rideId);
            return "Ride " + rideId + " does not exist.";
        }

        if (currentRide.isCompleted()) {
            logger.warn("Ride {} is already completed, cannot cancel.", rideId);
            return "Ride " + rideId + " is already completed.";
        }

        // Update ride status
        currentRide.setStatus(RideStatus.CANCELED);
        currentRide.setCompleted(true);
        rideRepository.save(currentRide);

        // Check if Rider exists
        if (currentRide.getRider() == null) {
            logger.warn("Ride {} has no associated rider.", rideId);
            return "Ride " + rideId + " successfully canceled (no rider).";
        }

        // Check if Driver exists before calling getDriverId()
        if (currentRide.getDriver() != null) {
            releaseDriver(currentRide.getDriver().getDriverId());
            logger.info("Driver {} released for canceled ride {}.", currentRide.getDriver().getDriverId(), rideId);
        } else {
            logger.warn("Ride {} has no associated driver.", rideId);
        }

        logger.info("Ride {} canceled successfully.", rideId);
        return "Ride " + rideId + " successfully canceled.";
    }


    @Override
    public String billRide(int rideId) {
        Ride currentRide = rideRepository.findById(rideId).orElse(null);
        if (currentRide == null) {
            logger.warn("Ride {} not found, cannot generate bill.", rideId);
            return "Ride " + rideId + " does not exist.";
        }

        if (currentRide.getRideFare() != 0) {
            logger.info("Ride {} already has a fare calculated: Rs. {}", rideId, currentRide.getRideFare());
            return "Total fare of the ride " + rideId + " is Rs. " + currentRide.getRideFare();
        }

        double distance = calculateDistance(
                currentRide.getPickupCoordinateX(), currentRide.getPickupCoordinateY(),
                currentRide.getDestinationCoordinateX(), currentRide.getDestinationCoordinateY()
        );
        double timeFare = 2 * currentRide.getTimeTaken();
        double distanceFare = 6.5 * distance;
        double baseFare = 50;
        double totalFare = (baseFare + distanceFare + timeFare) * 1.2; // Including 20% tax

        currentRide.setRideFare(totalFare);
        rideRepository.save(currentRide);

        logger.info("Ride {} billed successfully. Total fare: Rs. {}", rideId, totalFare);
        return "Total fare of the ride " + rideId + " is Rs. " + totalFare;
    }


    private void releaseDriver(int driverId) {


        driverRepository.findById(driverId).ifPresent(driver -> {
            driver.setAvailable(true);
            driverRepository.save(driver);
            logger.info("Driver {} marked as available.", driverId);
        });
    }

    @Override
    public double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }



    @Override
    public String startRide(int driverId,int rideId) {
        Ride selectedRide=rideRepository.findById(rideId).orElse(null);
        if (selectedRide == null) {
            logger.warn("Ride {} not found, cannot start it .", rideId);
            return "Ride " + rideId + " does not exist.";
        }

        if (selectedRide.getStatus()==RideStatus.AWAITING_PICKUP && selectedRide.getDriver().getDriverId()==driverId ){
            selectedRide.setStatus(RideStatus.STARTED);
            rideRepository.save(selectedRide);
            return "Ride with ID : "+ rideId+" is started successfully";
        }

        return "Ride with ID : "+ rideId+" cannot be started because of either status is not RideStatus.AWAITING_PICKUP or trying to started a unassigned ride ";


    }

    /**
     * Fetches the ride history of a specific rider.
     * @param riderId The ID of the rider.
     * @return List of rides associated with the specified rider.
     */
    @Override
    public List<Ride> getRideHistoryByRider(int riderId) {
        logger.info("Fetching ride history for Rider ID: {}", riderId);
        List<Ride> riderRides = rideRepository.findByRider_RiderId(riderId);

        if (riderRides.isEmpty()) {
            logger.warn("No ride history found for Rider ID: {}", riderId);
        } else {
            logger.info("Found {} rides for Rider ID: {}", riderRides.size(), riderId);
        }

        return riderRides;
    }

    /**
     * Fetches the ride history of a specific driver.
     * @param driverId The ID of the driver.
     * @return List of rides associated with the specified driver.
     */
    @Override
    public List<Ride> getRideHistoryByDriver(int driverId) {
        logger.info("Fetching ride history for Driver ID: {}", driverId);
        List<Ride> driverRides = rideRepository.findByDriver_DriverId(driverId);

        if (driverRides.isEmpty()) {
            logger.warn("No ride history found for Driver ID: {}", driverId);
        } else {
            logger.info("Found {} rides for Driver ID: {}", driverRides.size(), driverId);
        }

        return driverRides;
    }



}
