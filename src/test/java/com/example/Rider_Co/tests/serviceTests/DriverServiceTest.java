package com.example.Rider_Co.tests.serviceTests;

import com.example.Rider_Co.models.*;
import com.example.Rider_Co.repositories.*;
import com.example.Rider_Co.services.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceTest {

    @InjectMocks
    private DriverService driverService;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RideRepository rideRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDrivers() {
        List<Driver> mockList = List.of(new Driver(), new Driver());
        when(driverRepository.findAll()).thenReturn(mockList);
        List<Driver> result = driverService.GetAllDrivers();
        assertEquals(2, result.size());
        verify(driverRepository, times(1)).findAll();
    }

    @Test
    void testGetDriverByID_Found() {
        Driver mockDriver = new Driver();
        mockDriver.setDriverId(1);
        when(driverRepository.findById(1)).thenReturn(Optional.of(mockDriver));
        Driver result = driverService.GetDriverByID(1);
        assertEquals(0, result.getDriverId());
    }

    @Test
    void testGetDriverByID_NotFound() {
        when(driverRepository.findById(999)).thenReturn(Optional.empty());
        Driver result = driverService.GetDriverByID(999);
        assertNotNull(result); // returns a new Driver()
    }



    @Test
    void testUpdateDriver_DriverExists() {
        Driver driver = new Driver();
        driver.setDriverId(1);
        when(driverRepository.existsById(1)).thenReturn(true);
        String result = driverService.UpdateDriver(driver);
        assertTrue(result.contains("Successfully updated"));
        verify(driverRepository).save(driver);
    }

    @Test
    void testUpdateDriver_DriverNotExists() {
        Driver driver = new Driver();
        driver.setDriverId(2);
        when(driverRepository.existsById(2)).thenReturn(false);
        String result = driverService.UpdateDriver(driver);
        assertTrue(result.contains("does not exist"));
    }

    @Test
    void testDeleteDriver_DriverExists() {
        when(driverRepository.existsById(1)).thenReturn(true);
        String result = driverService.DeleteDriver(1);
        assertTrue(result.contains("deleted"));
        verify(driverRepository).deleteById(1);
    }

    @Test
    void testDeleteDriver_DriverNotExists() {
        when(driverRepository.existsById(5)).thenReturn(false);
        String result = driverService.DeleteDriver(5);
        assertTrue(result.contains("does not exist"));
    }

    @Test
    void testAcceptRide_Success() {
        Ride mockRide = new Ride();
        mockRide.setRideId(1);
        mockRide.setStatus(RideStatus.AVAILABLE_RIDE);
        mockRide.setRideAccepted(false);
        when(rideRepository.findById(1)).thenReturn(Optional.of(mockRide));
        Driver mockDriver = new Driver();
        mockDriver.setDriverId(1);
        when(driverRepository.findById(1)).thenReturn(Optional.of(mockDriver));
        String result = driverService.AcceptRide(1, 1);
        assertTrue(result.contains("RIDE_ACCEPTED"));
        verify(rideRepository).save(mockRide);
        verify(driverRepository).save(mockDriver);
    }

    @Test
    void testAcceptRide_RideNotFound() {
        when(rideRepository.findById(1)).thenReturn(Optional.empty());
        String result = driverService.AcceptRide(1, 1);
        assertFalse(result.contains("Ride is already assigned or does not exist"));
    }

    @Test
    void testAcceptRide_DriverNotFound() {
        Ride mockRide = new Ride();
        mockRide.setRideId(1);
        when(rideRepository.findById(1)).thenReturn(Optional.of(mockRide));
        when(driverRepository.findById(1)).thenReturn(Optional.empty());
        String result = driverService.AcceptRide(1, 1);
        assertTrue(result.contains("Driver doesn't exists"));
    }



    @Test
    void testGetAvailableRidesForDriver_DriverNotFound() {
        when(driverRepository.findById(999)).thenReturn(Optional.empty());
        List<Ride> result = driverService.getAvailableRidesForDriver(999);
        assertTrue(result.isEmpty());
    }
}
