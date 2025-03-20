package com.example.Rider_Co.controllers;

import com.example.Rider_Co.models.Ride;
import com.example.Rider_Co.serviceInterfaces.RideServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ride")
public class RideController {

    private static final Logger logger = LoggerFactory.getLogger(RideController.class);

    @Autowired
    private RideServiceInterface rideService;

    /**
     * Retrieves a list of all rides.
     *
     * @return List of rides.
     * @status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Ride>> getAllRides() {
        logger.info("Fetching all rides...");
        List<Ride> rides = rideService.getAllRides();
        return ResponseEntity.ok(rides); // 200 OK
    }

    /**
     * Retrieves a ride by its ID.
     *
     * @param rideId Ride ID.
     * @return Ride details if found, otherwise error message.
     * @status 200 OK | 404 NOT FOUND
     */
    @GetMapping("/{rideId}")
    public ResponseEntity<?> getRideById(@PathVariable int rideId) {
        logger.info("Fetching ride with ID: {}", rideId);
        Ride ride = rideService.getRideByID(rideId);

        if (ride == null) {
            logger.warn("Ride with ID {} not found!", rideId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ride not found!"); // 404 NOT FOUND
        }
        return ResponseEntity.ok(ride); // 200 OK
    }

    /**
     * Deletes a ride.
     *
     * @param rideId Ride ID.
     * @return Deletion status message.
     * @status 200 OK | 404 NOT FOUND
     */
    @DeleteMapping("/{rideId}")
    public ResponseEntity<String> deleteRide(@PathVariable int rideId) {
        logger.info("Deleting ride with ID: {}", rideId);
        String message = rideService.deleteRide(rideId);
        if (message.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); // 404 NOT FOUND
        }
        return ResponseEntity.ok(message); // 200 OK
    }

    /**
     * Stops a ride and records the time taken.
     *
     * @param rideId     Ride ID.
     * @param timeTaken  Time taken for the ride.
     * @return Stop ride status message.
     * @status 200 OK | 400 BAD REQUEST | 404 NOT FOUND
     */
    @PutMapping("/stop/{rideId}")
    public ResponseEntity<String> stopRide(@PathVariable int rideId, @RequestBody double timeTaken) {
        logger.info("Stopping ride with ID: {}. Time taken: {} minutes", rideId, timeTaken);
        String response = rideService.stopRide(rideId, timeTaken);
        if (response.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404 NOT FOUND
        }
        if (response.contains("already completed")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 BAD REQUEST
        }
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * Starts a ride with a driver.
     *
     * @param driverId Driver ID.
     * @param rideId   Ride ID.
     * @return Start ride status message.
     * @status 200 OK | 400 BAD REQUEST | 404 NOT FOUND
     */
    @PutMapping("/start/{driverId}/{rideId}")
    public ResponseEntity<String> startRide(@PathVariable int driverId, @PathVariable int rideId) {
        logger.info("Driver {} is starting ride {}", driverId, rideId);
        String response = rideService.startRide(driverId, rideId);
        if (response.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404 NOT FOUND
        }
        if (response.contains("cannot be started")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 BAD REQUEST
        }
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * Generates the bill for a ride.
     *
     * @param rideId Ride ID.
     * @return Bill details.
     * @status 200 OK | 404 NOT FOUND
     */
    @GetMapping("/bill/{rideId}")
    public ResponseEntity<String> billRide(@PathVariable int rideId) {
        logger.info("Generating bill for ride ID: {}", rideId);
        String response = rideService.billRide(rideId);
        if (response.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404 NOT FOUND
        }
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * Cancels a ride.
     *
     * @param rideId Ride ID.
     * @return Cancellation status message.
     * @status 200 OK | 400 BAD REQUEST | 404 NOT FOUND
     */
    @PutMapping("/cancel/{rideId}")
    public ResponseEntity<String> cancelRide(@PathVariable int rideId) {
        logger.info("Cancelling ride with ID: {}", rideId);
        String response = rideService.cancelRide(rideId);
        if (response.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404 NOT FOUND
        }
        if (response.contains("already completed")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 BAD REQUEST
        }
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * Get ride history for a specific rider.
     */
    @GetMapping("/history/rider/{riderId}")
    public List<Ride> getRideHistoryByRider(@PathVariable int riderId) {
        return rideService.getRideHistoryByRider(riderId);
    }

    /**
     * Get ride history for a specific driver.
     */
    @GetMapping("/history/driver/{driverId}")
    public List<Ride> getRideHistoryByDriver(@PathVariable int driverId) {
        return rideService.getRideHistoryByDriver(driverId);
    }
}
