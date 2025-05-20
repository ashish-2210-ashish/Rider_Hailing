package com.example.Rider_Co.tests.controllerTests;


import com.example.Rider_Co.controllers.RideController;
import com.example.Rider_Co.models.Ride;
import com.example.Rider_Co.serviceInterfaces.RideServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RideControllerTest {

    @Mock
    private RideServiceInterface rideService;

    @InjectMocks
    private RideController rideController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRides() {
        Ride ride = new Ride();
        when(rideService.getAllRides()).thenReturn(Collections.singletonList(ride));

        ResponseEntity<List<Ride>> response = rideController.getAllRides();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetRideByIdFound() {
        Ride ride = new Ride();
        when(rideService.getRideByID(1)).thenReturn(ride);

        ResponseEntity<?> response = rideController.getRideById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ride, response.getBody());
    }

    @Test
    void testGetRideByIdNotFound() {
        when(rideService.getRideByID(999)).thenReturn(null);

        ResponseEntity<?> response = rideController.getRideById(999);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ride not found!", response.getBody());
    }

    @Test
    void testDeleteRideSuccess() {
        when(rideService.deleteRide(1)).thenReturn("Successfully deleted ride with ID: 1");

        ResponseEntity<String> response = rideController.deleteRide(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Successfully deleted ride with ID: 1", response.getBody());
    }

    @Test
    void testDeleteRideNotFound() {
        when(rideService.deleteRide(999)).thenReturn("Ride with ID: 999 does not exist.");

        ResponseEntity<String> response = rideController.deleteRide(999);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ride with ID: 999 does not exist.", response.getBody());
    }

    @Test
    void testStopRideSuccess() {
        when(rideService.stopRide(1, 30.0)).thenReturn("Ride 1 successfully stopped.");

        ResponseEntity<String> response = rideController.stopRide(1, 30.0);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ride 1 successfully stopped.", response.getBody());
    }

    @Test
    void testStopRideAlreadyCompleted() {
        when(rideService.stopRide(1, 30.0)).thenReturn("Ride 1 is already completed.");

        ResponseEntity<String> response = rideController.stopRide(1, 30.0);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Ride 1 is already completed.", response.getBody());
    }

    @Test
    void testStopRideNotFound() {
        when(rideService.stopRide(999, 15.0)).thenReturn("Ride 999 does not exist.");

        ResponseEntity<String> response = rideController.stopRide(999, 15.0);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ride 999 does not exist.", response.getBody());
    }

    @Test
    void testStartRideSuccess() {
        when(rideService.startRide(10, 1)).thenReturn("Ride 1 started by driver 10.");

        ResponseEntity<String> response = rideController.startRide(10, 1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ride 1 started by driver 10.", response.getBody());
    }

    @Test
    void testStartRideNotFound() {
        when(rideService.startRide(10, 999)).thenReturn("Ride 999 does not exist.");

        ResponseEntity<String> response = rideController.startRide(10, 999);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ride 999 does not exist.", response.getBody());
    }

    @Test
    void testStartRideBadRequest() {
        when(rideService.startRide(10, 1)).thenReturn("Ride 1 cannot be started.");

        ResponseEntity<String> response = rideController.startRide(10, 1);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Ride 1 cannot be started.", response.getBody());
    }

    @Test
    void testBillRideSuccess() {
        when(rideService.billRide(1)).thenReturn("Total bill: $20.00");

        ResponseEntity<String> response = rideController.billRide(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Total bill: $20.00", response.getBody());
    }

    @Test
    void testBillRideNotFound() {
        when(rideService.billRide(999)).thenReturn("Ride 999 does not exist.");

        ResponseEntity<String> response = rideController.billRide(999);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ride 999 does not exist.", response.getBody());
    }

    @Test
    void testCancelRideSuccess() {
        when(rideService.cancelRide(1)).thenReturn("Ride 1 successfully canceled.");

        ResponseEntity<String> response = rideController.cancelRide(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ride 1 successfully canceled.", response.getBody());
    }

    @Test
    void testCancelRideAlreadyCompleted() {
        when(rideService.cancelRide(1)).thenReturn("Ride 1 is already completed.");

        ResponseEntity<String> response = rideController.cancelRide(1);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Ride 1 is already completed.", response.getBody());
    }

    @Test
    void testCancelRideNotFound() {
        when(rideService.cancelRide(999)).thenReturn("Ride 999 does not exist.");

        ResponseEntity<String> response = rideController.cancelRide(999);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ride 999 does not exist.", response.getBody());
    }

    @Test
    void testGetRideHistoryByRider() {
        Ride ride1 = new Ride();
        Ride ride2 = new Ride();
        when(rideService.getRideHistoryByRider(1)).thenReturn(Arrays.asList(ride1, ride2));

        List<Ride> result = rideController.getRideHistoryByRider(1);

        assertEquals(2, result.size());
    }

    @Test
    void testGetRideHistoryByDriver() {
        Ride ride1 = new Ride();
        when(rideService.getRideHistoryByDriver(2)).thenReturn(Collections.singletonList(ride1));

        List<Ride> result = rideController.getRideHistoryByDriver(2);

        assertEquals(1, result.size());
    }
}
