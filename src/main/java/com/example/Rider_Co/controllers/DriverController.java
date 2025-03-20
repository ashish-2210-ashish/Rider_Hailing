package com.example.Rider_Co.controllers;

import com.example.Rider_Co.models.Driver;
import com.example.Rider_Co.models.Ride;
import com.example.Rider_Co.serviceInterfaces.DriverServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private static final Logger logger = LoggerFactory.getLogger(DriverController.class);

    @Autowired
    private DriverServiceInterface driverService;

    /**
     * Retrieves a list of all drivers.
     * @return List of drivers and HTTP status
     */
    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        try {
            logger.info("Fetching all drivers");
            List<Driver> drivers = driverService.GetAllDrivers();
            return ResponseEntity.ok(drivers);
        } catch (Exception e) {
            logger.error("Error fetching all drivers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves a driver by their ID.
     * @param driverId ID of the driver
     * @return Driver object or NOT FOUND if driver is not found
     */
    @GetMapping("/{driverId}")
    public ResponseEntity<?> getDriverById(@PathVariable int driverId) {
        try {
            logger.info("Fetching driver with ID: {}", driverId);
            Driver driver = driverService.GetDriverByID(driverId);
            if (driver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found with ID: " + driverId);
            }
            return ResponseEntity.ok(driver);
        } catch (Exception e) {
            logger.error("Error fetching driver with ID: {}", driverId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching driver");
        }
    }

    /**
     * Adds a new driver.
     * @param driver Driver object to add
     * @return Success message and status
     */
    @PostMapping
    public ResponseEntity<String> addDriver(@RequestBody Driver driver) {
        try {
            logger.info("Adding new driver: {}", driver);
            String result = driverService.AddDriver(driver);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error adding driver", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add driver");
        }
    }

    /**
     * Updates driver details.
     * @param driverId ID of the driver to update
     * @param driver Updated driver details
     * @return Success message and status
     */
    @PutMapping("/{driverId}")
    public ResponseEntity<String> updateDriver(@PathVariable int driverId, @RequestBody Driver driver) {
        try {
            logger.info("Updating driver with ID: {}", driverId);
            driver.setDriverId(driverId);
            String result = driverService.UpdateDriver(driver);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error updating driver with ID: {}", driverId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update driver");
        }
    }

    /**
     * Deletes a driver.
     * @param driverId ID of the driver to delete
     * @return Success message and status
     */
    @DeleteMapping("/{driverId}")
    public ResponseEntity<String> deleteDriver(@PathVariable int driverId) {
        try {
            logger.info("Deleting driver with ID: {}", driverId);
            String result = driverService.DeleteDriver(driverId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error deleting driver with ID: {}", driverId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete driver");
        }
    }

    /**
     * Allows a driver to accept a ride.
     * @param driverId ID of the driver accepting the ride
     * @param rideId ID of the ride being accepted
     * @return Success message and status
     */
    @PostMapping("/acceptRide/{driverId}/{rideId}")
    public ResponseEntity<String> acceptRide(@PathVariable int driverId, @PathVariable int rideId) {
        try {
            logger.info("Driver with ID {} is accepting ride with ID {}", driverId, rideId);
            String result = driverService.AcceptRide(driverId, rideId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error accepting ride with ID: {} by driver with ID: {}", rideId, driverId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to accept ride");
        }
    }

    /**
     * Retrieves available rides for a specific driver.
     * @param driverId ID of the driver
     * @return List of available rides and HTTP status
     */
    @GetMapping("/availableRides/{driverId}")
    public ResponseEntity<List<Ride>> getAvailableRides(@PathVariable int driverId) {
        try {
            logger.info("Fetching available rides for driver with ID: {}", driverId);
            List<Ride> rides = driverService.getAvailableRidesForDriver(driverId);
            return ResponseEntity.ok(rides);
        } catch (Exception e) {
            logger.error("Error fetching available rides for driver with ID: {}", driverId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
