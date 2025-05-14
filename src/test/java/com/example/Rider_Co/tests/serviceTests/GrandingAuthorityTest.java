package com.example.Rider_Co.tests.serviceTests;



import com.example.Rider_Co.Authority.GrandingAuthority;
import com.example.Rider_Co.models.User;
import com.example.Rider_Co.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GrandingAuthorityTest {

    @Mock
    private UserRepository userRepository;

    private GrandingAuthority grandingAuthority;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        grandingAuthority = new GrandingAuthority(userRepository);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setRole("admin");

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = grandingAuthority.loadUserByUsername("john_doe");

        // Assert
        assertEquals("john_doe", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> grandingAuthority.loadUserByUsername("nonexistent")
        );

        assertEquals("USER NOT FOUND", exception.getMessage());
    }
}
