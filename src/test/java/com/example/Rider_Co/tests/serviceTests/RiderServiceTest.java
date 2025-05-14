package com.example.Rider_Co.tests.serviceTests;

import com.example.Rider_Co.models.*;
import com.example.Rider_Co.repositories.*;
import com.example.Rider_Co.services.RiderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RiderServiceTest {

    @InjectMocks
    private RiderService riderService;

    @Mock
    private RiderRepository riderRepository;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRiders() {
        List<Rider> mockList = List.of(new Rider(), new Rider());
        when(riderRepository.findAll()).thenReturn(mockList);

        List<Rider> result = riderService.getAllRiders();

        assertEquals(2, result.size());
        verify(riderRepository, times(1)).findAll();
    }

    @Test
    void testGetRiderById_Found() {
        Rider mockRider = new Rider();
        mockRider.setRiderId(1);
        when(riderRepository.findById(1)).thenReturn(Optional.of(mockRider));

        Rider result = riderService.getRiderByID(1);

        assertEquals(1, result.getRiderId());
    }

    @Test
    void testGetRiderById_NotFound() {
        when(riderRepository.findById(999)).thenReturn(Optional.empty());

        Rider result = riderService.getRiderByID(999);

        assertNotNull(result); // returns a new Rider()
    }

    @Test
    void testAddRider_UserExists() {
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        User mockUser = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        Rider rider = new Rider();
        rider.setRiderId(1);

        String result = riderService.addRider(rider);

        assertTrue(result.contains("Successfully added"));
        verify(riderRepository).save(any(Rider.class));
    }

    @Test
    void testAddRider_UserNotExists() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn("nonexistentuser");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        Rider rider = new Rider();
        String result = riderService.addRider(rider);

        assertTrue(result.contains("user doesn't exists"));
    }

    @Test
    void testUpdateRider_RiderExists() {
        Rider rider = new Rider();
        rider.setRiderId(1);
        when(riderRepository.existsById(1)).thenReturn(true);

        String result = riderService.updateRider(rider);

        assertTrue(result.contains("Successfully updated"));
        verify(riderRepository).save(rider);
    }

    @Test
    void testUpdateRider_RiderNotExists() {
        Rider rider = new Rider();
        rider.setRiderId(2);
        when(riderRepository.existsById(2)).thenReturn(false);

        String result = riderService.updateRider(rider);

        assertTrue(result.contains("does not exist"));
    }

    @Test
    void testDeleteRider_RiderExists() {
        when(riderRepository.existsById(1)).thenReturn(true);

        String result = riderService.deleteRider(1);

        assertTrue(result.contains("deleted"));
        verify(riderRepository).deleteById(1);
    }

    @Test
    void testDeleteRider_RiderNotExists() {
        when(riderRepository.existsById(5)).thenReturn(false);

        String result = riderService.deleteRider(5);

        assertTrue(result.contains("does not exist"));
    }

    @Test
    void testMatchDrivers_Success() {
        Rider rider = new Rider();
        rider.setRiderId(1);
        rider.setCoordinateX(1.0);
        rider.setCoordinateY(2.0);

        when(riderRepository.findById(1)).thenReturn(Optional.of(rider));
        when(rideRepository.findByRider_RiderIdAndIsCompleted(1, false)).thenReturn(Collections.emptyList());

        String result = riderService.matchDrivers(1, 5.0, 5.0);

        assertTrue(result.contains("Successfully added the ride"));
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void testMatchDrivers_RiderNotFound() {
        when(riderRepository.findById(999)).thenReturn(Optional.empty());

        String result = riderService.matchDrivers(999, 0, 0);

        assertEquals("RIDER_NOT_FOUND", result);
    }

    @Test
    void testMatchDrivers_AlreadyHasRide() {
        Rider rider = new Rider();
        rider.setRiderId(1);
        when(riderRepository.findById(1)).thenReturn(Optional.of(rider));
        when(rideRepository.findByRider_RiderIdAndIsCompleted(1, false)).thenReturn(List.of(new Ride()));

        String result = riderService.matchDrivers(1, 1, 1);

        assertTrue(result.contains("RIDE_ALREADY_EXISTS"));
    }
}
