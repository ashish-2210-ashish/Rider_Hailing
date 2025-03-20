package com.example.Rider_Co.controllers;

import com.example.Rider_Co.dto.MatchRequestDto;
import com.example.Rider_Co.models.Rider;
import com.example.Rider_Co.serviceInterfaces.RiderServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rider")
public class RiderController {

    private static final Logger logger = LoggerFactory.getLogger(RiderController.class);

    @Autowired
    private RiderServiceInterface riderService;

    /**
     * Retrieves all riders.
     * @return List of riders with HTTP 200 status.
     */
    @GetMapping
    public ResponseEntity<List<Rider>> getAllRiders() {
        logger.info("Fetching all riders...");
        return ResponseEntity.ok(riderService.getAllRiders());
    }

    /**
     * Retrieves a rider by ID.
     * @param riderId ID of the rider.
     * @return Rider details or 404 if not found.
     */
    @GetMapping("/{riderId}")
    public ResponseEntity<?> getRiderById(@PathVariable int riderId) {
        logger.info("Fetching rider with ID: {}", riderId);
        Rider rider = riderService.getRiderByID(riderId);

        if (rider == null) {
            logger.warn("Rider with ID {} not found!", riderId);
            return ResponseEntity.status(404).body("Rider not found!");
        }
        return ResponseEntity.ok(rider);
    }

    /**
     * Adds a new rider.
     * @param rider Rider details.
     * @return Success message with HTTP 201 status.
     */
    @PostMapping
    public ResponseEntity<String> addRider(@RequestBody Rider rider) {
        logger.info("Adding new rider: {}", rider);
        return ResponseEntity.status(201).body(riderService.addRider(rider));
    }

    /**
     * Updates a rider's details.
     * @param riderId ID of the rider to update.
     * @param rider Updated rider details.
     * @return Success or failure message.
     */
    @PutMapping("/{riderId}")
    public ResponseEntity<String> updateRider(@PathVariable int riderId, @RequestBody Rider rider) {
        logger.info("Updating rider with ID: {}", riderId);
        rider.setRiderId(riderId);
        return ResponseEntity.ok(riderService.updateRider(rider));
    }

    /**
     * Deletes a rider from the system.
     * @param riderId ID of the rider to delete.
     * @return Success or failure message.
     */
    @DeleteMapping("/{riderId}")
    public ResponseEntity<String> deleteRider(@PathVariable int riderId) {
        logger.info("Deleting rider with ID: {}", riderId);
        return ResponseEntity.ok(riderService.deleteRider(riderId));
    }

    /**
     * Matches a rider with available drivers based on destination coordinates.
     * @param riderId ID of the rider.
     * @param matchRequestDto Destination coordinates.
     * @return Matching response.
     */
    @PostMapping("/match/{riderId}")
    public ResponseEntity<String> matchDrivers(@PathVariable int riderId, @RequestBody MatchRequestDto matchRequestDto) {
        double endX = matchRequestDto.getDestinationCoordinateX();
        double endY = matchRequestDto.getDestinationCoordinateY();
        logger.info("Matching drivers for rider ID: {} with destination coordinates ({}, {})", riderId, endX, endY);

        return ResponseEntity.ok(riderService.matchDrivers(riderId, endX, endY));
    }
}
