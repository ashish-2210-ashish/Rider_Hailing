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


    @Test
    void testGetRideById_InvalidId() {
        when(rideRepository.findById(0)).thenReturn(Optional.empty());
        Ride result = rideService.getRideByID(0);
        assertNotNull(result);
        verify(rideRepository).findById(0);
    }

    @Test
    void testGetAllRides_EmptyDatabase() {
        when(rideRepository.findAll()).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.getAllRides();
        assertTrue(result.isEmpty());
        verify(rideRepository).findAll();
    }

    @Test
    void testCancelRide_RideAlreadyCompleted() {
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setStatus(RideStatus.COMPLETED);
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        String result = rideService.cancelRide(1);
        assertEquals("Ride 1 successfully canceled (no rider).", result);
    }

    @Test
    void testCancelRide_RideWithoutDriver() {
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setStatus(RideStatus.AWAITING_PICKUP);
        ride.setDriver(null);  // No driver assigned
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        String result = rideService.cancelRide(1);
        assertEquals("Ride 1 successfully canceled (no rider).", result);
    }

    @Test
    void testCancelRide_RideNotFound() {
        when(rideRepository.findById(99)).thenReturn(Optional.empty());
        String result = rideService.cancelRide(99);
        assertEquals("Ride 99 does not exist.", result);
    }




    @Test
    void testStartRide_InvalidRide() {
        when(rideRepository.findById(99)).thenReturn(Optional.empty());
        String result = rideService.startRide(101, 99);
        assertEquals("Ride 99 does not exist.", result);
    }

    @Test
    void testStartRide_InvalidDriver() {
        Driver driver = new Driver();
        driver.setDriverId(101);
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setStatus(RideStatus.STARTED);
        ride.setDriver(driver);  // Driver assigned but ride status already started
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        String result = rideService.startRide(102, 1); // Wrong driver ID
        assertEquals("Ride with ID : 1 cannot be started because of either status is not RideStatus.AWAITING_PICKUP or trying to started a unassigned ride ", result);
    }

    @Test
    void testBillRide_NullRide() {
        when(rideRepository.findById(99)).thenReturn(Optional.empty());
        String result = rideService.billRide(99);
        assertEquals("Ride 99 does not exist.", result);
    }

    @Test
    void testBillRide_AlreadyBilled() {
        Ride ride = new Ride();
        ride.setRideId(2);
        ride.setRideFare(BigDecimal.valueOf(200)); // Already billed
        when(rideRepository.findById(2)).thenReturn(Optional.of(ride));
        String result = rideService.billRide(2);
        assertEquals("Total fare of the ride 2 is Rs. 200", result);
    }

    @Test
    void testCalculateDistance_NegativeCoordinates() {
        double distance = rideService.calculateDistance(-1, -1, 2, 2);
        assertEquals(4.242640687119285, distance, 0.0001);
    }

    @Test
    void testCalculateDistance_SameCoordinates() {
        double distance = rideService.calculateDistance(0, 0, 0, 0);
        assertEquals(0.0, distance);
    }




    @Test
    void testCancelRide_RideDoesNotExist() {
        when(rideRepository.findById(99)).thenReturn(Optional.empty());
        String result = rideService.cancelRide(99);
        assertEquals("Ride 99 does not exist.", result);
    }


    @Test
    void testCancelRide_SuccessWithNoDriver() {
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setStatus(RideStatus.AWAITING_PICKUP);
        ride.setDriver(null); // No driver assigned
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        String result = rideService.cancelRide(1);
        assertEquals("Ride 1 successfully canceled (no rider).", result);
    }



    @Test
    void testBillRide_ZeroDistance() {
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setPickupCoordinateX(0);
        ride.setPickupCoordinateY(0);
        ride.setDestinationCoordinateX(0); // Same coordinates (Zero distance)
        ride.setDestinationCoordinateY(0);
        ride.setTimeTaken(10);
        ride.setRideFare(BigDecimal.ZERO);
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        String result = rideService.billRide(1);
        assertTrue(result.contains("Total fare of the ride 1 is Rs. "));
    }

    @Test
    void testStartRide_RideAlreadyStarted() {
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setStatus(RideStatus.STARTED);
        Driver driver = new Driver();
        driver.setDriverId(101);
        ride.setDriver(driver);
        when(rideRepository.findById(1)).thenReturn(Optional.of(ride));
        String result = rideService.startRide(101, 1);
        assertTrue(result.contains("cannot be started"));
    }


    @Test
    void testGetRideHistoryByRider_NoRidesFound() {
        when(rideRepository.findByRider_RiderId(99)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.getRideHistoryByRider(99);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRideHistoryByDriver_NoRidesFound() {
        when(rideRepository.findByDriver_DriverId(99)).thenReturn(Collections.emptyList());
        List<Ride> result = rideService.getRideHistoryByDriver(99);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRideHistoryByRider_RideNotAssigned() {
        Ride ride = new Ride();
        ride.setRideId(1);
        ride.setRider(new Rider());
        when(rideRepository.findByRider_RiderId(1)).thenReturn(List.of(ride));
        List<Ride> result = rideService.getRideHistoryByRider(1);
        assertFalse(result.isEmpty());
    }


}
