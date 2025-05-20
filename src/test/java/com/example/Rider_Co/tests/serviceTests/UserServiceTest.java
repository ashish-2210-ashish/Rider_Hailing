package com.example.Rider_Co.tests.serviceTests;

import com.example.Rider_Co.models.User;
import com.example.Rider_Co.repositories.UserRepository;
import com.example.Rider_Co.services.UserService;
import com.example.Rider_Co.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_returnsSuccess_whenValidUser() {
        User user = User.builder()
                .username("testuser")
                .password("password")
                .role("RIDER")
                .build();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        String result = userService.registerUser(user);
        assertThat(result).isEqualTo("User registered successfully!");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_returnsError_whenUsernameExists() {
        User user = User.builder()
                .username("existingUser")
                .password("password")
                .role("DRIVER")
                .build();
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));
        String result = userService.registerUser(user);
        assertThat(result).isEqualTo("Username already taken");
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticate_returnsUser_whenPasswordMatches() {
        String rawPassword = "password";
        String encodedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(rawPassword);
        User user = User.builder()
                .username("john")
                .password(encodedPassword)
                .role("RIDER")
                .build();
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        Optional<User> result = userService.authenticate("john", rawPassword);
        assertThat(result).isPresent();
    }

    @Test
    void authenticate_returnsEmpty_whenPasswordMismatch() {
        User user = User.builder()
                .username("john")
                .password(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("secret"))
                .role("DRIVER")
                .build();
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        Optional<User> result = userService.authenticate("john", "wrong");
        assertThat(result).isEmpty();
    }

    @Test
    void handleLogin_returnsOk_whenValidCredentials() {
        User user = User.builder()
                .username("testuser")
                .password("password")
                .role("RIDER")
                .build();
        String encodedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("password");
        User storedUser = User.builder()
                .username("testuser")
                .password(encodedPassword)
                .role("RIDER")
                .build();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(storedUser));
        when(jwtUtil.generateToken("testuser", "RIDER")).thenReturn("mocked-jwt");
        ResponseEntity<?> response = userService.handleLogin(user, httpServletResponse);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("cookie created correctly");
    }

    @Test
    void handleLogin_returnsUnauthorized_whenInvalidCredentials() {
        User user = User.builder()
                .username("testuser")
                .password("wrongpass")
                .role("RIDER")
                .build();
        String encodedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("password");
        User storedUser = User.builder()
                .username("testuser")
                .password(encodedPassword)
                .role("RIDER")
                .build();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(storedUser));
        ResponseEntity<?> response = userService.handleLogin(user, httpServletResponse);
        assertThat(response.getStatusCodeValue()).isEqualTo(401);
        assertThat(response.getBody()).isEqualTo("Invalid username or password");
    }
}

