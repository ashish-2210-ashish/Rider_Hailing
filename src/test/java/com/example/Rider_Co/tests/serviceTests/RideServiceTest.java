package com.example.Rider_Co.tests.serviceTests;

import com.example.Rider_Co.models.*;
import com.example.Rider_Co.repositories.DriverRepository;
import com.example.Rider_Co.repositories.RideRepository;
import com.example.Rider_Co.repositories.RiderRepository;
import com.example.Rider_Co.serviceInterfaces.DriverServiceInterface;
import com.example.Rider_Co.services.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RideServiceTest {

    @InjectMocks
    private RideService rideService;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private RiderRepository riderRepository;

    @Mock
    private DriverServiceInterface driverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRides() {
        List<Ride> mockRides = List.of(new Ride(), new Ride());
        when(rideRepository.findAll()).thenReturn(mockRides);
        List<Ride> result = rideService.getAllRides();
        assertEquals(2, result.size());
        verify(rideRepository, times(1)).findAll();
    }

    @Test
    void testGetRideById_RideExists() {
        Ride ride = new Ride();
        ride.setRideId(1);
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        Ride result = rideService.getRideByID(1);
        assertEquals(1, result.getRideId());
    }

    @Test
    void testGetRideById_RideDoesNotExist() {
        when(rideRepository.findById(99)).thenReturn(Optional.empty());
        Ride result = rideService.getRideByID(99);
        assertNotNull(result);
    }

    @Test
    void testDeleteRide_Exists() {
        when(rideRepository.existsById(1)).thenReturn(true);
        String result = rideService.deleteRide(1);
        assertEquals("Successfully deleted ride with ID: 1", result);
        verify(rideRepository).deleteById(1);
    }

    @Test
    void testDeleteRide_NotExists() {
        when(rideRepository.existsById(99)).thenReturn(false);
        String result = rideService.deleteRide(99);
        assertEquals("Ride with ID: 99 does not exist.", result);
    }

    @Test
    void testBillRide_FareNotCalculated() {
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setPickupCoordinateX(0);
        ride.setPickupCoordinateY(0);
        ride.setDestinationCoordinateX(3);
        ride.setDestinationCoordinateY(4);
        ride.setTimeTaken(10);
        ride.setRideFare(BigDecimal.ZERO);
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        String result = rideService.billRide(1);
        assertTrue(result.contains("Total fare of the ride 1 is Rs. "));
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void testStartRide_Success() {
        Driver driver = new Driver();
        driver.setDriverId(101);
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setStatus(RideStatus.AWAITING_PICKUP);
        ride.setDriver(driver);
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        String result = rideService.startRide(101, 1);
        assertEquals("Ride with ID : 1 is started successfully", result);
        verify(rideRepository).save(ride);
    }

    @Test
    void testStartRide_FailureDueToWrongDriverOrStatus() {
        Driver driver = new Driver();
        driver.setDriverId(101);
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setStatus(RideStatus.STARTED);
        ride.setDriver(driver);
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        String result = rideService.startRide(101, 1);
        assertTrue(result.contains("cannot be started"));
    }

    @Test
    void testCalculateDistance() {
        double distance = rideService.calculateDistance(0, 0, 3, 4);
        assertEquals(5.0, distance);
    }

    @Test
    void testGetRideHistoryByRider() {
        List<Ride> mockRides = List.of(new Ride(), new Ride());
        when(rideRepository.findByRider_RiderId(1)).thenReturn(mockRides);
        List<Ride> result = rideService.getRideHistoryByRider(1);
        assertEquals(2, result.size());
    }

    @Test
    void testGetRideHistoryByDriver() {
        List<Ride> mockRides = List.of(new Ride(), new Ride(), new Ride());
        when(rideRepository.findByDriver_DriverId(1)).thenReturn(mockRides);
        List<Ride> result = rideService.getRideHistoryByDriver(1);
        assertEquals(3, result.size());
    }

}
